package com.alibaba.json.bvt.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.json.bvtVO.ae.Section;
import junit.framework.TestCase;

import java.util.Arrays;

/**
 * Created by wenshao on 08/05/2017.
 */
public class AETest extends TestCase {
    public void test_for_ae() throws Exception {
        Section section = new Section();

        section.children = Arrays.<com.alibaba.json.bvtVO.ae.Area>asList(new Section());

        String json = JSON.toJSONString(section, SerializerFeature.WriteClassName);

        Section section2 = (Section) JSON.parseObject(json, com.alibaba.json.bvtVO.ae.Area.class);
        assertNotNull(section2.children);
    }
}
