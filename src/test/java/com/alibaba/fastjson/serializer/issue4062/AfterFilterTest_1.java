package com.alibaba.fastjson.serializer.issue4062;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.AfterFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AfterFilterTest_1 {
    //CS304 (manually written) Issue link: https://github.com/alibaba/fastjson/issues/4062
    @Test
    public void testIssue4062_1(){
        AfterFilter afterFilterTest = new AfterFilterTest();

        List<UserVO> users = new ArrayList<UserVO>(2);

        users.add(new UserVO(0,"a",0));
        users.add(new UserVO(1,"b",1));
        String s1= JSON.toJSONString(users,afterFilterTest);
        String s2=JSON.toJSONString(users,afterFilterTest, SerializerFeature.DisableCircularReferenceDetect);
        Assert.assertEquals(s1,s2);
    }
}
