package com.alibaba.json.bvt.bug;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class TestJSONMap extends TestCase {
    protected void setUp() throws Exception {
        com.alibaba.fastjson.parser.ParserConfig.global.addAccept("com.alibaba.json.bvt.bug.TestJSONMap.");
    }

    public void test_0() throws Exception {
        Record record = new Record();
        Map map = new HashMap();
        record.setRecord(map);
        String s = JSON.toJSONString(record, SerializerFeature.WriteClassName);
        System.out.println(s);
        record = (Record)JSON.parse(s); //此处抛出异常
        System.out.println(record.getRecord().size());
    }

    public static class Record {

        private Map<Integer, Integer> record;

        public Map<Integer, Integer> getRecord() {
            return record;
        }

        public void setRecord(Map<Integer, Integer> record) {
            this.record = record;
        }
    }
}
