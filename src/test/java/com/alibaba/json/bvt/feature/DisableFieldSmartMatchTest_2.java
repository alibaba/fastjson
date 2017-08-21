package com.alibaba.json.bvt.feature;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

/**
 * Created by wenshao on 17/03/2017.
 */
public class DisableFieldSmartMatchTest_2 extends TestCase {
    public void test_feature() throws Exception {
        assertEquals(123, JSON.parseObject("{\"person_id\":123}", Model_for_disableFieldSmartMatchMask.class).personId);
        assertEquals(0, JSON.parseObject("{\"person_id\":123}", Model_for_disableFieldSmartMatchMask.class, Feature.DisableFieldSmartMatch).personId);
        assertEquals(123, JSON.parseObject("{\"personId\":123}", Model_for_disableFieldSmartMatchMask.class, Feature.DisableFieldSmartMatch).personId);
    }

    public void test_feature2() throws Exception {
        assertEquals(0, JSON.parseObject("{\"person_id\":123}", Model_for_disableFieldSmartMatchMask2.class).personId);
        assertEquals(123, JSON.parseObject("{\"personId\":123}", Model_for_disableFieldSmartMatchMask2.class).personId);
    }

    public static class Model_for_disableFieldSmartMatchMask {
        public int personId;
    }

    public static class Model_for_disableFieldSmartMatchMask2 {
        @JSONField(parseFeatures = Feature.DisableFieldSmartMatch)
        public int personId;
    }
}
