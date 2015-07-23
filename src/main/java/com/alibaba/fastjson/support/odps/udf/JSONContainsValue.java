package com.alibaba.fastjson.support.odps.udf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.aliyun.odps.udf.UDF;

public class JSONContainsValue extends UDF {

    public JSONContainsValue(){
        SerializeConfig.getGlobalInstance().setAsmEnable(false);
    }

    public Boolean evaluate(String jsonString, String path, String value) throws Exception {
        try {
            Object json = JSON.parse(jsonString);
            return JSONPath.containsValue(json, path, value);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Boolean evaluate(String jsonString, String path, Long value) throws Exception {
        try {
            Object json = JSON.parse(jsonString);
            return JSONPath.containsValue(json, path, value);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Boolean evaluate(String jsonString, String path, Boolean value) throws Exception {
        try {
            Object json = JSON.parse(jsonString);
            return JSONPath.containsValue(json, path, value);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Boolean evaluate(String jsonString, String path, Double value) throws Exception {
        try {
            Object json = JSON.parse(jsonString);
            return JSONPath.containsValue(json, path, value);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
