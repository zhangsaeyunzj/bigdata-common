package com.nuonuo.bigdata.common;

/**
 * 自定义主键生成接口
 * <p>
 * create by zhangbl 2017-09-14
 */
public interface RowKeyGenerator {

    public String generateKey(String... params);
}
