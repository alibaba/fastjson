package com.alibaba.fastjson.deserializer.issue3752;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.deserializer.issue3752.beans.Source;
import com.alibaba.fastjson.deserializer.issue3752.beans.SourceList;
import org.junit.Assert;
import org.junit.Test;


/**
 * https://github.com/alibaba/fastjson/issues/3752
 *
 * @author ruan
 * @date 2021/05/10
 */
public class TestIssue3752 {

    @Test
    public void testIssue3752() {
        Source src = new Source();
        SourceList srcList = src.getCourseList();
        srcList.add("TestIssue3752");
        src.setCourseList(srcList);
        String json = "{\"courseList\":[\"TestIssue3752\"]}";
        Source src2 = JSONObject.parseObject(json, Source.class);
        Assert.assertEquals(src, src2);
    }

}