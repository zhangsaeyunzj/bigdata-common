package com.nuonuo.bigdata.utils;

import com.nuonuo.bigdata.entity.InvoiceMain;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class HBaseUtilsTest {

    @Before
    public void init() {
        System.out.println("init");
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", "hm,hb,hs1");
        config.set("hbase.zookeeper.property.clientPort", "2181");

        try {
            HBaseUtils.init(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void batchPutTest() {
        List<InvoiceMain> list = new ArrayList<InvoiceMain>();

        for (int i = 0; i < 10; i++) {
            InvoiceMain invoiceMain = new InvoiceMain();
            invoiceMain.setBuyerAccount("dfasdf");
            invoiceMain.setInvoiceKind("s");
            invoiceMain.setInvoiceNum("312341324" + i);
            invoiceMain.setInvoiceCode("330902");
            invoiceMain.setInvoiceDate(new Date());
            invoiceMain.setNotaxAmountSum(234234.334d);
            invoiceMain.setBillFlag("1");
            List<Map<String,String>> detail = new ArrayList<>();

            Map<String,String> map1 = new HashMap<>();
            map1.put("name","zbl");

            Map<String,String> map2 = new HashMap<>();
            map2.put("name","zbl111");
            detail.add(map1);
            detail.add(map2);
            invoiceMain.setDetail(detail);
            list.add(invoiceMain);
        }
        try {
            HBaseUtils.batchPut("zbl_test11", "info", list, 0, InvoiceMain.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void putTest() {
        InvoiceMain invoiceMain = new InvoiceMain();
        invoiceMain.setBuyerAccount("dfasdf");
        invoiceMain.setInvoiceKind("s");
        invoiceMain.setInvoiceNum("999999999");
        invoiceMain.setInvoiceCode("330902");
        invoiceMain.setInvoiceDate(new Date());
        invoiceMain.setNotaxAmountSum(234234.334d);
        invoiceMain.setBillFlag("1");

        try {
            HBaseUtils.put("zbl_test11", "info", invoiceMain, 0, InvoiceMain.class);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getTest() {
        try {
            System.out.println(HBaseUtils.get("zbl_test11", "999999999330902s"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteRowTest() {
        try {
            HBaseUtils.deleteRow("zbl_test11", "999999999330902s", 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
