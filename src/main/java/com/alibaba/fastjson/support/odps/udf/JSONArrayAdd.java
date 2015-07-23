package com.alibaba.fastjson.support.odps.udf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.aliyun.odps.udf.UDF;

public class JSONArrayAdd extends UDF {

    public JSONArrayAdd(){
        SerializeConfig.getGlobalInstance().setAsmEnable(false);
        ParserConfig.getGlobalInstance().setAsmEnable(false);
    }

    public String evaluate(String jsonString, String path, String... values) throws Exception {
        try {
            Object json = JSON.parse(jsonString);
            JSONPath.arrayAdd(json, path, values);
            return JSON.toJSONString(json);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String evaluate(String jsonString, String path, Long... values) throws Exception {
        try {
            Object json = JSON.parse(jsonString);
            JSONPath.arrayAdd(json, path, values);
            return JSON.toJSONString(json);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String evaluate(String jsonString, String path, Boolean... values) throws Exception {
        try {
            Object json = JSON.parse(jsonString);
            JSONPath.arrayAdd(json, path, values);
            return JSON.toJSONString(json);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String evaluate(String jsonString, String path, Double... values) throws Exception {
        try {
            Object json = JSON.parse(jsonString);
            JSONPath.arrayAdd(json, path, values);
            return JSON.toJSONString(json);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
