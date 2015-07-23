package com.alibaba.fastjson.support.odps.udf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.aliyun.odps.udf.UDF;

public class JSONSet extends UDF {

    public JSONSet(){
        SerializeConfig.getGlobalInstance().setAsmEnable(false);
        ParserConfig.getGlobalInstance().setAsmEnable(false);
    }

    public String evaluate(String jsonString, String path, String value) throws Exception {
        try {
            Object json = JSON.parse(jsonString);
            JSONPath.set(json, path, value);
            return JSON.toJSONString(json);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String evaluate(String jsonString, String path, Long value) throws Exception {
        try {
            Object json = JSON.parse(jsonString);
            JSONPath.set(json, path, value);
            return JSON.toJSONString(json);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String evaluate(String jsonString, String path, Boolean value) throws Exception {
        try {
            Object json = JSON.parse(jsonString);
            JSONPath.set(json, path, value);
            return JSON.toJSONString(json);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String evaluate(String jsonString, String path, Double value) throws Exception {
        try {
            Object json = JSON.parse(jsonString);
            JSONPath.set(json, path, value);
            return JSON.toJSONString(json);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
