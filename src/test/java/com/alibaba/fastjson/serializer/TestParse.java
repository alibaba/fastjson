package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Logger;

/**
 * test parse json contains jsonobject in javabean
 * Created by yixian on 2016-02-25.
 */
public class TestParse {
    private final Logger logger = Logger.getLogger(TestParse.class.getSimpleName());

    private String jsonString;

    @Before
    public void prepareJsonString() {
        TestBean bean = new TestBean();
        bean.setName("tester");
        JSONObject data = new JSONObject();
        data.put("key", "value");
        bean.setData(data);
        jsonString = JSON.toJSONString(bean, SerializerFeature.WriteClassName);
    }

    @Test
    public void testParse() {
        logger.info("parsing json string:" + jsonString);
        TestBean testBean = (TestBean) JSON.parse(jsonString);
        assert testBean.getData() != null;
        assert "tester".equals(testBean.getName());
        assert "value".equals(testBean.getData().getString("key"));
    }
}
