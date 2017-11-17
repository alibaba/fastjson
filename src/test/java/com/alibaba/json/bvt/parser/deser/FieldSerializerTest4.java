package com.alibaba.json.bvt.parser.deser;

import com.google.common.collect.Lists;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created By maxiaoyao
 * Date: 2017/10/8
 * Time: 下午10:52
 */
public class FieldSerializerTest4 {
    @Test
    public void testPattern() {
        Result<List> listResult = new Result<List>(Lists.newArrayList());
        Result<Boolean> booleanResult = new Result<Boolean>(null);
        String listJson = JSON.toJSONString(
                listResult,
                SerializerFeature.PrettyFormat
        );
        String booleanJson = JSON.toJSONString(
                booleanResult,
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullListAsEmpty
        );
        Assert.assertEquals("{\n\t\"data\":[]\n}", listJson);
        Assert.assertEquals("{\n\t\n}", booleanJson);
    }

    private static class Result<T>{
        private T data;

        public Result(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }
    }
}
