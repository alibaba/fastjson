package com.alibaba.fastjson.support.odps.udf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.aliyun.odps.udf.UDF;

public class GetJSONObject extends UDF {
    public GetJSONObject() {
        // SerializeConfig.getGlobalInstance().setAsmEnable(false);
    }

    public String evaluate(String jsonString, String path) throws Exception {
        Object json = JSON.parse(jsonString);
        JSONPath json_path = new JSONPath(path);
        Object result = json_path.eval(json);
        return JSON.toJSONString(result);
    }
}
