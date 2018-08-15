package com.alibaba.json.bvt.parser.autoType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import junit.framework.TestCase;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by wenshao on 10/02/2017.
 */
public class AutoTypeTest4 extends TestCase {
    ConcurrentMap<String,Class<?>> mappings;
    ParserConfig                   config = new ParserConfig();

    protected void setUp() throws Exception {
        Field field = TypeUtils.class.getDeclaredField("mappings");
        field.setAccessible(true);

        mappings = (ConcurrentMap<String,Class<?>>) field.get(null);
    }

    public void test_0() throws Exception {
        String payload="{\"@type\":\"java.lang.Class\",\"val\":\"com.sun.rowset.JdbcRowSetImpl\"}";
        String payload_2 = "{\"@type\":\"com.sun.rowset.JdbcRowSetImpl\",\"dataSourceName\":\"rmi://127.0.0.1:8889/xxx\",\"autoCommit\":true}";

        assertNotNull("class deser is not null", config.getDeserializer(Class.class));

        int size = mappings.size();

        final int COUNT = 10;
        for (int i = 0; i < COUNT; ++i){
            JSON.parse(payload, config);
        }

        for (int i = 0; i < COUNT; ++i){
            Throwable error2 = null;
            try {
                JSON.parseObject(payload_2);
            } catch (Exception e) {
                error2 = e;
            }
            assertNotNull(error2);
            assertEquals(JSONException.class, error2.getClass());
        }

        assertEquals(size, mappings.size());
    }

    public void test_dns() throws Exception {
        String f2 = "{\"@type\":\"java.net.InetAddress\",\"val\":\"baidu.com\"}";

        Throwable error = null;
        try {
            JSON.parse(f2, config);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_3() throws Exception {
        String f2 = "{\"@type\":\"java.net.InetAddress\",\"val\":\"baidu.com\"}";

        Throwable error = null;
        try {
            JSON.parse(f2, config);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_4() throws Exception {
        int size = mappings.size();
        JSON.parse("{\"@type\":\"com.alibaba.json.bvt.parser.autoType.AutoTypeTest4$Model\"}", new ParserConfig());

        assertEquals(size + 1, mappings.size());
    }

    public void test_5() throws Exception {
        int size = mappings.size();
        JSON.parse("{\"@type\":\"com.alibaba.json.bvt.parser.autoType.AutoTypeTest4$Model1\"}", new ParserConfig());

        assertEquals(size + 1, mappings.size());
    }

    public void test_6() throws Exception {
        int size = mappings.size();
        JSON.parseObject("{\"@type\":\"com.alibaba.json.bvt.parser.autoType.AutoTypeTest4$Model2\"}", Model1.class);

        assertEquals(size + 1, mappings.size());
    }

    public void test_7() throws Exception {
        int size = mappings.size();
        JSON.parse("{\"@type\":\"com.alibaba.json.bvt.parser.autoType.AutoTypeTest4$Model3\"}", Feature.SupportAutoType);

        assertEquals(size + 1, mappings.size());
    }

    public void test_8() throws Exception {
        config.setAutoTypeSupport(true);
        int size = mappings.size();

        JSON.parse("{\"@type\":\"com.alibaba.json.bvt.parser.autoType.AutoTypeTest4$Model4\"}", config, JSON.DEFAULT_PARSER_FEATURE);

        assertEquals(size + 1, mappings.size());
        config.setAutoTypeSupport(false);
    }

    public void test_9() throws Exception {
        int size = mappings.size();

        JSON.parse("{\"@type\":\"java.lang.Class\",\"val\":\"com.alibaba.json.bvt.parser.autoType.AutoTypeTest4$Model5\"}", config);

        assertEquals(size, mappings.size());
    }

    @JSONType
    public static class Model {
        static int i = 0;
        static {
            i = 3;
        }
    }

    @JSONType
    public static class Model1 {

    }

    @JSONType
    public static class Model2 extends Model1 {

    }

    public static class Model3 {

    }

    public static class Model4 {

    }

    public static class Model5 {

    }

    public void test_10_ser() throws Exception {
        String f2 = "{\"value\":{\"@type\":\"com.alibaba.json.bvt.parser.autoType.AutoTypeTest4$Model3\"}}";

        Throwable error = null;
        try {
            JSON.parseObject(f2, Model6.class, config);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public static class Model6 {
        public AutoCloseable value;
    }

    public void test_3_obj() throws Exception {
        String f2 = "{\"@type\":\"com.alibaba.json.bvt.parser.autoType.AutoTypeTest4$Model7\"}";

        Throwable error = null;
        try {
            JSON.parseObject(f2, Object.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_3_ser() throws Exception {
        String f2 = "{\"@type\":\"com.alibaba.json.bvt.parser.autoType.AutoTypeTest4$Model7\"}";

        Throwable error = null;
        try {
            JSON.parseObject(f2, Serializable.class);
        } catch (JSONException ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public static class Model7 {

    }
}
