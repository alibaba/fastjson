package com.alibaba.fastjson.serializer.issue4062;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BeforeFilterTest_2 {
    //CS304 (manually written) Issue link: https://github.com/alibaba/fastjson/issues/4062
    @Test
    public void testIssue4152_2(){
        BeforeFilterTest beforeFilterTest = new BeforeFilterTest();

        List<UserVO> users = new ArrayList<UserVO>(2);

        users.add(new UserVO(0,"#",0));
        users.add(new UserVO(1,"123",1));
        users.add(new UserVO(2,"$",2));
        String s1= JSON.toJSONString(users,beforeFilterTest);
        String s2=JSON.toJSONString(users,beforeFilterTest, SerializerFeature.DisableCircularReferenceDetect);
        Assert.assertEquals(s1,s2);
    }
}
