package com.alibaba.json.bvt.issue_3000;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

public class Issue3065 extends TestCase {
    public void test_for_issue() throws Exception {
        String data = "{\n" +
                "\t\"code\":\"OK\",\n" +
                "\t\"data\":[\n" +
                "\t\t{\n" +
                "\t\t\t\"createTime\":1584457789,\n" +
                "\t\t\t\"dbName\":\"basic_test\",\n" +
                "\t\t\t\"lastAccessTime\":0,\n" +
                "\t\t\t\"parameters\":{\n" +
                "\t\t\t\t\"transient_lastDdlTime\":\"1584457789\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"parametersSize\":2,\n" +
                "\t\t\t\"partId\":2209,\n" +
                "\t\t\t\"sd\":{\n" +
                "\t\t\t\t\"bucketCols\":[],\n" +
                "\t\t\t\t\"cdId\":2719,\n" +
                "\t\t\t\t\"cols\":[\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\":\"n_nationkey\",\n" +
                "\t\t\t\t\t\t\"type\":\"int\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\":\"n_name\",\n" +
                "\t\t\t\t\t\t\"type\":\"string\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\":\"n_regionkey\",\n" +
                "\t\t\t\t\t\t\"type\":\"int\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\":\"n_comment\",\n" +
                "\t\t\t\t\t\t\"type\":\"string\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"compressed\":false,\n" +
                "\t\t\t\t\"inputFormat\":\"org.apache.hadoop.mapred.TextInputFormat\",\n" +
                "\t\t\t\t\"location\":\"oss://hello/world/\",\n" +
                "\t\t\t\t\"numBuckets\":0,\n" +
                "\t\t\t\t\"outputFormat\":\"org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat\",\n" +
                "\t\t\t\t\"parameters\":{},\n" +
                "\t\t\t\t\"sdId\":2662,\n" +
                "\t\t\t\t\"serDeInfo\":{\n" +
                "\t\t\t\t\t\"name\":\"nation_part_hidden\",\n" +
                "\t\t\t\t\t\"parameters\":{\n" +
                "\t\t\t\t\t\t\"field.delim\":\"|\",\n" +
                "\t\t\t\t\t\t\"serialization.format\":\"|\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"serdeId\":2720,\n" +
                "\t\t\t\t\t\"serializationLib\":\"org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"skewedInfo\":{\n" +
                "\t\t\t\t\t\"skewedColNames\":[],\n" +
                "\t\t\t\t\t\"skewedColValueLocationMaps\":{},\n" +
                "\t\t\t\t\t\"skewedColValues\":[]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"sortCols\":[],\n" +
                "\t\t\t\t\"storedAsSubDirectories\":false\n" +
                "\t\t\t},\n" +
                "\t\t\t\"tableName\":\"nation_part_hidden\",\n" +
                "\t\t\t\"tblId\":453,\n" +
                "\t\t\t\"values\":[\n" +
                "\t\t\t\t\"2019\",\n" +
                "\t\t\t\t\"01\",\n" +
                "\t\t\t\t\"15\"\n" +
                "\t\t\t]\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"createTime\":1584457789,\n" +
                "\t\t\t\"dbName\":\"basic_test\",\n" +
                "\t\t\t\"lastAccessTime\":0,\n" +
                "\t\t\t\"parameters\":{\n" +
                "\t\t\t\t\"transient_lastDdlTime\":\"1584457789\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"parametersSize\":2,\n" +
                "\t\t\t\"partId\":2210,\n" +
                "\t\t\t\"sd\":{\n" +
                "\t\t\t\t\"bucketCols\":[],\n" +
                "\t\t\t\t\"cdId\":2719,\n" +
                "\t\t\t\t\"cols\":[\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\":\"n_nationkey\",\n" +
                "\t\t\t\t\t\t\"type\":\"int\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\":\"n_name\",\n" +
                "\t\t\t\t\t\t\"type\":\"string\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\":\"n_regionkey\",\n" +
                "\t\t\t\t\t\t\"type\":\"int\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\":\"n_comment\",\n" +
                "\t\t\t\t\t\t\"type\":\"string\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"compressed\":false,\n" +
                "\t\t\t\t\"inputFormat\":\"org.apache.hadoop.mapred.TextInputFormat\",\n" +
                "\t\t\t\t\"location\":\"oss://hello/world/\",\n" +
                "\t\t\t\t\"numBuckets\":0,\n" +
                "\t\t\t\t\"outputFormat\":\"org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat\",\n" +
                "\t\t\t\t\"parameters\":{\n" +
                "\t\t\t\t\t\"$ref\":\"$[0].sd.parameters\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"sdId\":2663,\n" +
                "\t\t\t\t\"serDeInfo\":{\n" +
                "\t\t\t\t\t\"name\":\"nation_part_hidden\",\n" +
                "\t\t\t\t\t\"parameters\":{\n" +
                "\t\t\t\t\t\t\"$ref\":\"$[0].sd.serDeInfo.parameters\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"serdeId\":2721,\n" +
                "\t\t\t\t\t\"serializationLib\":\"org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"skewedInfo\":{\n" +
                "\t\t\t\t\t\"skewedColNames\":[],\n" +
                "\t\t\t\t\t\"skewedColValueLocationMaps\":{},\n" +
                "\t\t\t\t\t\"skewedColValues\":[]\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"sortCols\":[],\n" +
                "\t\t\t\t\"storedAsSubDirectories\":false\n" +
                "\t\t\t},\n" +
                "\t\t\t\"tableName\":\"nation_part_hidden\",\n" +
                "\t\t\t\"tblId\":453,\n" +
                "\t\t\t\"values\":[\n" +
                "\t\t\t\t\"2018\",\n" +
                "\t\t\t\t\"12\",\n" +
                "\t\t\t\t\"20\"\n" +
                "\t\t\t]\n" +
                "\t\t}\n" +
                "\t],\n" +
                "\t\"success\":true\n" +
                "}";
        ResultData resultData = JSON.parseObject(data, ResultData.class);
        System.out.println(resultData);
    }

    public static class ResultData
    {
        private boolean success;
        private String message;
        private Object data;
    }
}
