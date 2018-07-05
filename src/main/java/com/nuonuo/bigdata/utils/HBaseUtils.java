package com.nuonuo.bigdata.utils;

import com.alibaba.fastjson.JSON;
import com.nuonuo.bigdata.annotation.HBaseField;
import com.nuonuo.bigdata.annotation.RowKey;
import com.nuonuo.bigdata.common.RowKeyGenerator;
import com.nuonuo.bigdata.common.RowKeyType;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by zhangbl on 2017-09-18.
 * <p>
 * 操作hbase的工具类
 * <p>
 * <b>example:</b>
 * <br>
 * <p>
 * Configuration config = HBaseConfiguration.create();<br>
 * config.set("hbase.zookeeper.quorum", "hm,hb,hs1");<br>
 * config.set("hbase.zookeeper.property.clientPort", "2181");<br>
 * HBaseUtils.init(config);<br>
 * HBaseUtils.xxx();<br>
 */
public class HBaseUtils {

    private static HConnection hConnection;
    private static volatile boolean isInit = false;

    public static void init(Configuration config) throws IOException {
        if (!isInit) {
            hConnection = HConnectionManager.createConnection(config);
            isInit = true;
        }
    }

    public static HConnection getHConnection() {
        if (!isInit) {
            throw new RuntimeException("please init HConnection at first...");
        } else {
            return hConnection;
        }
    }

    /**
     * 根据rowkey进行查询
     *
     * @param tableName 表名
     * @param rowkey    主键
     * @return {@code Map<String, String>}
     * @throws IOException
     */
    public static Map<String, Object> get(final String tableName, final String rowkey) throws IOException {
        Map<String, Object> result = new HashMap<>();
        HTableInterface table = null;
        try {
            table = getHConnection().getTable(tableName);

            Get get = new Get(Bytes.toBytes(rowkey));
            Result r = table.get(get);

            return result2Map(r);
        } finally {
            if (null != table) {
                table.close();
            }
        }
    }

    /**
     * 批量插入hbase
     *
     * @param tableName 表名
     * @param col       列簇名
     * @param list      待插入的List
     * @param ts        时间戳
     * @param clazz     T的实际类型
     * @param <T>
     * @throws IOException
     * @throws IllegalAccessException
     */
    public static <T> void batchPut(final String tableName, final String col, final List<T> list, final long ts,
                                    final Class clazz) throws IOException, IllegalAccessException, InstantiationException {
        HTableInterface table = null;
        try {
            table = getHConnection().getTable(tableName);
            List<Put> putList = new ArrayList<Put>();
            String[] rs = null;
            for (T t : list) {
                if (!clazz.isInstance(t)) {
                    throw new RuntimeException("T must conform clazz " + clazz);
                }

                String rowkey = generateRowKey(t.getClass().getAnnotation(RowKey.class), t, clazz);
                putList.add(assemblePut(rowkey, col, t, ts, clazz));
            }
            table.put(putList);
            table.flushCommits();
        } finally {
            if (null != table) {
                table.close();
            }
        }
    }

    /**
     * 单条数据插入到hbase
     *
     * @param tableName 表名
     * @param col       列簇
     * @param t         instance of clazz
     * @param ts        时间戳
     * @param clazz     需要映射的class
     * @param <T>       泛型T instance of clazz
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public static <T> void put(final String tableName, final String col, final T t, final long ts,
                               final Class clazz) throws InstantiationException, IllegalAccessException, IOException {
        if (!clazz.isInstance(t)) {
            throw new RuntimeException("T must conform clazz " + clazz);
        }
        HTableInterface table = null;
        try {
            table = getHConnection().getTable(tableName);
            String rowkey = generateRowKey(t.getClass().getAnnotation(RowKey.class), t, clazz);
            table.put(assemblePut(rowkey, col, t, ts, clazz));
            table.flushCommits();
        } finally {
            if (null != table) {
                table.close();
            }
        }
    }

    /**
     * 根据startRow和endRow来scan表
     *
     * @param tableName 表名
     * @param startRow  起始行
     * @param endRow    终止行
     * @return
     * @throws IOException
     */
    public static List<Map<String, Object>> scan(final String tableName, final String startRow, final String endRow) throws IOException {
        List<Map<String, Object>> result = new ArrayList<>();
        HTableInterface table = null;
        ResultScanner rs = null;
        try {
            table = getHConnection().getTable(tableName);
            Scan scan = new Scan(Bytes.toBytes(startRow), Bytes.toBytes(endRow));
            rs = table.getScanner(scan);
            Iterator<Result> iterator = rs.iterator();
            while (iterator.hasNext()) {
                Result r = iterator.next();
                result.add(result2Map(r));
            }
            return result;
        } finally {
            if (null != rs) {
                rs.close();
            }
            if (null != table) {
                table.close();
            }
        }
    }

    /**
     * 根据Filter 来scan表
     *
     * @param tableName 表名
     * @param filter
     * @return
     * @throws IOException
     */
    public static List<Map<String, Object>> scan(final String tableName, final Filter filter) throws IOException {
        List<Map<String, Object>> result = new ArrayList<>();
        HTableInterface table = null;
        ResultScanner rs = null;
        try {
            table = getHConnection().getTable(tableName);
            Scan scan = new Scan();
            scan.setFilter(filter);
            rs = table.getScanner(scan);
            Iterator<Result> iterator = rs.iterator();
            while (iterator.hasNext()) {
                Result r = iterator.next();
                result.add(result2Map(r));
            }
            return result;
        } finally {
            if (null != rs) {
                rs.close();
            }
            if (null != table) {
                table.close();
            }
        }
    }

    /**
     * 根据Filters来scan表
     *
     * @param tableName 表名
     * @param filters
     * @return
     * @throws IOException
     */
    public static List<Map<String, Object>> scan(final String tableName, final List<Filter> filters) throws IOException {
        List<Map<String, Object>> result = new ArrayList<>();
        HTableInterface table = null;
        ResultScanner rs = null;
        try {
            table = getHConnection().getTable(tableName);
            Scan scan = new Scan();
            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);
            scan.setFilter(filterList);
            rs = table.getScanner(scan);
            Iterator<Result> iterator = rs.iterator();
            while (iterator.hasNext()) {
                Result r = iterator.next();
                result.add(result2Map(r));
            }
            return result;
        } finally {
            if (null != rs) {
                rs.close();
            }
            if (null != table) {
                table.close();
            }
        }
    }

    /**
     * 组装hbase {@link Put}
     *
     * @param rowkey 主键
     * @param col    列簇
     * @param t      instance of clazz
     * @param ts     时间戳
     * @param clazz  需要映射的class
     * @param <T>    泛型T instance of clazz
     * @return
     * @throws IllegalAccessException
     */
    private static <T> Put assemblePut(final String rowkey, final String col, final T t, final long ts,
                                       final Class clazz) throws IllegalAccessException {
        Put put = new Put(Bytes.toBytes(rowkey));
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            Object obj = f.get(t);
            if (null == obj) {
                continue;
            }
            HBaseField hf = f.getAnnotation(HBaseField.class);
            //判断是否为需要忽略字段
            if (null != hf && hf.ignore()) {
                continue;
            }
            String qualifier = null;
            String format = null;
            String cf = hf != null ? hf.cf().equals("") ? col : hf.cf() : col;
            if (null == hf || hf.name().equals("")) {
                qualifier = f.getName();
            } else {
                qualifier = hf.name();
            }
            if (null == hf || hf.format().equals("")) {
                format = "yyyy-MM-dd";
            } else {
                format = hf.format();
            }
            //确定要插入hbase的value，假设是java.util.Date类型转化成java.lang.String
            String value = null;
            if (f.getType().getName().equals(Date.class.getName())) {
                DateFormat dateFormat = new SimpleDateFormat(format);
                value = dateFormat.format(obj);
            } else if (f.getType().getName().equals(List.class.getName())) {
                value = JSON.toJSONString(obj);
            } else {
                value = String.valueOf(f.get(t));
            }
            //是否带时间戳插入hbase
            if (ts != 0) {
                put.add(Bytes.toBytes(cf), Bytes.toBytes(qualifier), ts, Bytes.toBytes(value));
            } else {
                put.add(Bytes.toBytes(cf), Bytes.toBytes(qualifier), Bytes.toBytes(value));
            }
        }
        return put;
    }

    /**
     * 根据主键（时间戳）删除一整行
     *
     * @param tableName 表名
     * @param rowkey    主键
     * @param ts        时间戳
     * @throws IOException
     */
    public static void deleteRow(final String tableName, final String rowkey, final long ts) throws IOException {
        HTableInterface table = null;
        try {
            table = getHConnection().getTable(tableName);
            Delete delete = new Delete(Bytes.toBytes(rowkey));
            if (ts != 0) {
                delete.setTimestamp(ts);
            }
            table.delete(delete);
            table.flushCommits();
        } finally {
            if (null != table) {
                table.close();
            }
        }
    }

    /**
     * 根据rowkey进行批量删除，这里不能设置时间戳
     *
     * @param tableName 表名
     * @param rowkeys   rowkey数组
     * @throws IOException
     */
    public static void batchDeleteRow(final String tableName, final String[] rowkeys) throws IOException {
        HTableInterface table = null;
        try {
            table = getHConnection().getTable(tableName);
            List<Delete> deletes = new ArrayList<>();
            for (String rk : rowkeys) {
                Delete del = new Delete(Bytes.toBytes(rk));
                deletes.add(del);
            }
            table.delete(deletes);
            table.flushCommits();
        } finally {
            if (null != table) {
                table.close();
            }
        }
    }

    /**
     * 根据rowkey进行批量删除，这里不能设置时间戳
     *
     * @param tableName 表名
     * @param deletes   {@code List<Delete>}集合
     * @throws IOException
     */
    public static void batchDeleteRow(final String tableName, final List<Delete> deletes) throws IOException {
        HTableInterface table = null;
        try {
            table = getHConnection().getTable(tableName);
            table.delete(deletes);
            table.flushCommits();
        } finally {
            if (null != table) {
                table.close();
            }
        }
    }

    private static <T> String check(Field[] fields, String s, T t)
            throws IllegalAccessException {
        for (Field f : fields) {
            if (f.getName().equals(s)) {
                f.setAccessible(true);
                Object obj = f.get(t);
                if (null == obj) {
                    return null;
                } else {
                    return String.valueOf(obj);
                }
            }
        }
        return null;
    }

    /**
     * 生成主键的规则
     *
     * @param r     annotation {@link RowKey}
     * @param t     instance of clazz
     * @param clazz
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static <T> String generateRowKey(RowKey r, T t, Class clazz) throws IllegalAccessException, InstantiationException {
        String rowkey = "";
        if (r.method() == RowKeyType.UUID) {
            return UUID.randomUUID().toString().replace("-", "");
        } else if (r.method() == RowKeyType.TIMESTAMP) {
            return String.valueOf(System.currentTimeMillis());
        } else if (r.method() == RowKeyType.CUSTOM) {
            //封装自定义主键需要用到的字段
            String[] rs = r.field().split(",");
            List<String> params = new ArrayList<String>();
            for (String s : rs) {
                String sf = check(clazz.getDeclaredFields(), s, t);
                params.add(sf);
            }
            //实例化自定义主键封装类，并生成主键
            Class<? extends RowKeyGenerator> clazzName = r.clazzName();
            rowkey = clazzName.newInstance().generateKey(params.toArray(new String[params.size()]));

            //是否需要控制主键长度
            long rl = r.length();
            if (rl != 0) {
                while (rowkey.length() < rl) {
                    rowkey += "F";
                }
            }
        }
        return rowkey;
    }

    /**
     * 将 {@link Result} 转化成 java.util.Map
     *
     * @param r
     * @return
     */
    private static Map<String, Object> result2Map(Result r) {
        if (r.isEmpty()) {
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        for (Cell cell : r.listCells()) {
            String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
            String value = Bytes.toString(CellUtil.cloneValue(cell));
            result.put(qualifier, value);
        }
        return result;
    }
}
