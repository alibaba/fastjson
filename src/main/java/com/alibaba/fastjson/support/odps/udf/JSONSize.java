package com.alibaba.fastjson.support.odps.udf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.aliyun.odps.udf.UDF;

public class JSONSize extends UDF {

    public JSONSize(){
        SerializeConfig.getGlobalInstance().setAsmEnable(false);
    }

    public Long evaluate(String jsonString, String path) throws Exception {
        try {
            Object json = JSON.parse(jsonString);
            return Long.valueOf(JSONPath.size(json, path));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
