package com.alibaba.json.bvt.parser.deser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

import java.util.Locale;

/**
 * Created by wenshao on 14/03/2017.
 */
public class LocaleFieldTest extends TestCase {
    public void test_local_str() throws Exception {
        Model model = new Model();
        model.locale = Locale.CHINA;

        String json = JSON.toJSONString(model);

        JSONObject jsonObject = JSON.parseObject(json);
        jsonObject.toJavaObject(Model.class);
    }

    public void test_local_obj() throws Exception {
        String json = "{\"locale\":{\"displayCountry\":\"China\",\"displayVariant\":\"\",\"displayLanguage\":\"Chinese\",\"language\":\"zh\",\"displayName\":\"Chinese (China)\",\"variant\":\"\",\"ISO3Language\":\"zho\",\"ISO3Country\":\"CHN\",\"country\":\"CN\"}}";

        JSONObject jsonObject = JSON.parseObject(json);
        Model model2 = jsonObject.toJavaObject(Model.class);
        assertEquals("CN", model2.locale.getCountry());
        assertEquals("zh", model2.locale.getLanguage());
        assertEquals(Locale.CHINA.getDisplayCountry(), model2.locale.getDisplayCountry());
    }

    public static class Model {
        public Locale locale;
    }
}
