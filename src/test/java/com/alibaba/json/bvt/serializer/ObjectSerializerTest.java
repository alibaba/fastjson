package com.alibaba.json.bvt.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;

import junit.framework.TestCase;

public class ObjectSerializerTest extends TestCase {

    public void test_serialize() throws Exception {
        SerializeConfig config = new SerializeConfig();
        config.put(ResultCode.class, new ResultCodeSerilaizer());

        Result result = new Result();
        result.code = ResultCode.SIGN_ERROR;
        String json = JSON.toJSONString(result, config);
        Assert.assertEquals("{\"code\":17}", json);
    }

    public static class Result {

        public ResultCode code;
    }

    public static enum ResultCode {
                                   SUCCESS(1), ERROR(-1), UNKOWN_ERROR(999), LOGIN_FAILURE(8), INVALID_ARGUMENT(0),
                                   SIGN_ERROR(17);

        public final int value;

        ResultCode(int value){
            this.value = value;
        }
    }

    public static class ResultCodeSerilaizer implements ObjectSerializer {

        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                          int features) throws IOException {
            serializer.write(((ResultCode) object).value);
        }
    }
}
