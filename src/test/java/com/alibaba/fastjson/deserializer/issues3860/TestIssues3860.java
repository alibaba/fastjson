package com.alibaba.fastjson.deserializer.issues3860;

import java.lang.reflect.Method;
import java.util.HashMap;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.deserializer.issues3860.bean.TestApi;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author deqing.ldq
 * @description 修复issues3860: https://github.com/alibaba/fastjson/issues/3860
 * @date 2022/05/01 14:48
 *
 **/
public class TestIssues3860 {

    @Test
    public void testIssues3860() throws NoSuchMethodException {
        // 在TypeReference的类加载时，缓存池里会增加一个List<String>的缓存。
        Assert.assertEquals(1, TypeReference.getCacheSize());

        ParserConfig parserConfig = new ParserConfig();
        /**
         * 在反序列化时，type可以是外部传的变量，如果在使用场景，频繁通过这个进行反序列化：JSON.parseObject(in, type, JSON.DEFAULT_PARSER_FEATURE);
         * 通过匿名类的方式构建入参，模拟外部传参
         */
        Method method = TestApi.class.getMethod("getGenericClassA", null);

        int deserializerCount = parserConfig.getDeserializers().size();
        ObjectDeserializer deserializer1 = parserConfig.getDeserializer(method.getGenericReturnType());
        Assert.assertNotNull(deserializer1);
        Assert.assertEquals(++deserializerCount, parserConfig.getDeserializers().size());

        method = TestApi.class.getMethod("getGenericClassA", null);
        ObjectDeserializer deserializer2 = parserConfig.getDeserializer(method.getGenericReturnType());
        Assert.assertEquals(deserializer1, deserializer2);
        Assert.assertEquals(2, TypeReference.getCacheSize());
        Assert.assertEquals(deserializerCount, parserConfig.getDeserializers().size());
    }
}
