package com.nuonuo.bigdata.common;

public class ContactRowKeyGenerator implements RowKeyGenerator {
    @Override
    public String generateKey(String... params) {
        StringBuilder rowkey = new StringBuilder();
        for (String s : params) {
            rowkey.append(s);
        }
        return rowkey.toString();
    }
}
