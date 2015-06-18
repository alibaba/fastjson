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
        Object json = JSON.parse(jsonString);
        return JSONPath.containsValue(json, path, value);
    }
    
    public Boolean evaluate(String jsonString, String path, Long value) throws Exception {
        Object json = JSON.parse(jsonString);
        return JSONPath.containsValue(json, path, value);
    }
    
    public Boolean evaluate(String jsonString, String path, Boolean value) throws Exception {
        Object json = JSON.parse(jsonString);
        return JSONPath.containsValue(json, path, value);
    }
    
    public Boolean evaluate(String jsonString, String path, Double value) throws Exception {
        Object json = JSON.parse(jsonString);
        return JSONPath.containsValue(json, path, value);
    }
}
