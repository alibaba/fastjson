package com.alibaba.fastjson.jsonpath.issue3607;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author : ganyu
 * <p> @Date: 2021/1/6 10:57 </p>
 */
public class TestIssue3607 {

    @Test
    public void testIssue3607() {
        TestData testData = JSON.parseObject("{\n" +
                "    \"data\": {\n" +
                "        \"dataRows\": [\n" +
                "            {\n" +
                "                \"dataFields\": [\n" +
                "                    {\n" +
                "                        \"id\": 1332,\n" +
                "                        \"name\": \"gmtRegular\",\n" +
                "                        \"status\": \"success\",\n" +
                "                        \"valueType\": \"DATE\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"id\": 302,\n" +
                "                        \"name\": \"deptNo\",\n" +
                "                        \"status\": \"success\",\n" +
                "                        \"value\": \"C3736\",\n" +
                "                        \"valueType\": \"STRING\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"id\": 143,\n" +
                "                        \"name\": \"gmtOrigRegular\",\n" +
                "                        \"status\": \"success\",\n" +
                "                        \"value\": 1621126800000,\n" +
                "                        \"valueType\": \"DATE\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"id\": 135,\n" +
                "                        \"name\": \"name\",\n" +
                "                        \"status\": \"success\",\n" +
                "                        \"value\": \"\",\n" +
                "                        \"valueType\": \"STRING\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"id\": 133,\n" +
                "                        \"name\": \"workNo\",\n" +
                "                        \"status\": \"success\",\n" +
                "                        \"value\": \"29*6\",\n" +
                "                        \"valueType\": \"STRING\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"id\": 140,\n" +
                "                        \"name\": \"gmtEntry\",\n" +
                "                        \"status\": \"success\",\n" +
                "                        \"value\": 1605456000000,\n" +
                "                        \"valueType\": \"DATE\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"id\": 199,\n" +
                "                        \"name\": \"superWorkNo\",\n" +
                "                        \"status\": \"success\",\n" +
                "                        \"value\": \"240397\",\n" +
                "                        \"valueType\": \"STRING\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"status\": \"success\",\n" +
                "    \"success\": true\n" +
                "}", TestData.class);


        List<String> evalResult = (List<String>) JSONPath.eval(testData, "$.data.dataRows[*].dataFields[*].value");
        Assert.assertEquals(testData.getData().getDataRows().get(0).getDataFields().size(), evalResult.size());

    }

    static class TestData {
        private Test1 data;
        private String status;
        private Boolean success;

        public Test1 getData() {
            return data;
        }

        public void setData(Test1 data) {
            this.data = data;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }
    }

    static class Test1 {
        List<Test2> dataRows;

        public List<Test2> getDataRows() {
            return dataRows;
        }

        public void setDataRows(List<Test2> dataRows) {
            this.dataRows = dataRows;
        }
    }

    static class Test2 {
        List<Test3> dataFields;

        public List<Test3> getDataFields() {
            return dataFields;
        }

        public void setDataFields(List<Test3> dataFields) {
            this.dataFields = dataFields;
        }
    }

    static class Test3 {
        private Integer id;
        private String name;
        private String status;
        private String value;
        private String valueType;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValueType() {
            return valueType;
        }

        public void setValueType(String valueType) {
            this.valueType = valueType;
        }
    }

}

