package com.alibaba.json.bvt.bug;

import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Bug_for_agapple_2 extends TestCase {

    public void test_bug() throws Exception {
        DbMediaSource obj = new DbMediaSource();
        obj.setType(DataMediaType.ORACLE);
        
        JSONObject json = (JSONObject) JSON.toJSON(obj);
        Assert.assertEquals("ORACLE", json.get("type"));
    }

    public static class DbMediaSource {

        private DataMediaType type;

        public DataMediaType getType() {
            return type;
        }

        public void setType(DataMediaType type) {
            this.type = type;
        }

    }

    public static enum DataMediaType {
        ORACLE, MYSQL
    }
}
