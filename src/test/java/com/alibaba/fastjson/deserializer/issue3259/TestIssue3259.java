package com.alibaba.fastjson.deserializer.issue3259;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yiyanghua
 * @since 2020年06月11日
 * <p>
 * https://github.com/alibaba/fastjson/issues/3259
 */
public class TestIssue3259 {

    @Test
    public void testIssue1463() {
        Test4Vo test4Vo = new Test4Vo();
        test4Vo.setC(BigDecimal.TEN);
        test4Vo.setD(BigDecimal.ZERO);

        List<Test4Vo> test4VoList = new ArrayList<Test4Vo>();
        test4VoList.add(test4Vo);

        Test3Vo test3Vo = new Test3Vo();
        test3Vo.setTest4VoList(test4VoList);

        Test2Vo test2Vo = new Test2Vo();
        test2Vo.setA(test3Vo);
        test2Vo.setB(test4Vo);

        Test1Vo test1Vo = new Test1Vo();
        test1Vo.setTest2Vo(test2Vo);


        Map<Long, Test1Vo> test1VoMap = new HashMap<Long, Test1Vo>();
        test1VoMap.put(1L, test1Vo);
        Test0Vo test0Vo = new Test0Vo();
        test0Vo.setVoMap(test1VoMap);

        CommonResult<Test0Vo> cc = new CommonResult<Test0Vo>();
        cc.setResultData(test0Vo);


        Response message = new Response();
        message.setResponse(cc);

        SerializeConfig.getGlobalInstance().put(Response.class, ResponseSerializer.instance);
        ParserConfig.getGlobalInstance().putDeserializer(Response.class, ResponseDeserializer.instance);


        String r = JSON.toJSONString(message);
        Response c = JSON.parseObject(r, Response.class);

        // 反序列化出来C对象，丢了test2Vo的B属性
        Assert.assertEquals(r, JSON.toJSONString(c));

    }


}
