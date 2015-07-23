package com.alibaba.fastjson.support.odps.udf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.aliyun.odps.udf.UDF;

public class JSONContains extends UDF {

    public JSONContains(){
        SerializeConfig.getGlobalInstance().setAsmEnable(false);
    }

    public Boolean evaluate(String jsonString, String path) throws Exception {
        try {
            Object json = JSON.parse(jsonString);
            return JSONPath.contains(json, path);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
