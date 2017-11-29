package com.alibaba.json.bvt.support;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import junit.framework.TestCase;
import org.junit.Assert;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class FastJsonConfigTest extends TestCase {

    public void test_0() throws Exception {

        FastJsonConfig config = new FastJsonConfig();

        Assert.assertEquals(Charset.forName("UTF-8"), config.getCharset());
        config.setCharset(Charset.forName("GBK"));
        Assert.assertEquals(Charset.forName("GBK"), config.getCharset());

        Assert.assertNull(config.getDateFormat());
        config.setDateFormat("yyyyMMdd");
        Assert.assertNotNull(config.getDateFormat());

        config.setParserConfig(ParserConfig.getGlobalInstance());
        Assert.assertNotNull(config.getParserConfig());

        config.setSerializeConfig(SerializeConfig.globalInstance);
        Assert.assertNotNull(config.getSerializeConfig());

        config.setFeatures(Feature.AllowComment, Feature.AutoCloseSource);
        Assert.assertEquals(2, config.getFeatures().length);
        Assert.assertEquals(Feature.AllowComment, config.getFeatures()[0]);
        Assert.assertEquals(Feature.AutoCloseSource, config.getFeatures()[1]);

        config.setSerializerFeatures(SerializerFeature.IgnoreErrorGetter);
        Assert.assertEquals(1, config.getSerializerFeatures().length);
        Assert.assertEquals(SerializerFeature.IgnoreErrorGetter,
                config.getSerializerFeatures()[0]);

        config.setSerializeFilters(serializeFilter);
        Assert.assertEquals(1, config.getSerializeFilters().length);
        Assert.assertEquals(serializeFilter, config.getSerializeFilters()[0]);

        classSerializeFilter.put(TestVO.class, serializeFilter);
        config.setClassSerializeFilters(classSerializeFilter);
        Assert.assertEquals(1, config.getClassSerializeFilters().size());
        Assert.assertEquals(classSerializeFilter,
                config.getClassSerializeFilters());
        config.setClassSerializeFilters(null);

        config.setWriteContentLength(false);
        Assert.assertEquals(false, config.isWriteContentLength());
    }

    private Map<Class<?>, SerializeFilter> classSerializeFilter = new HashMap<Class<?>, SerializeFilter>();

    private SerializeFilter serializeFilter = new ValueFilter() {
        @Override
        public Object process(Object object, String name, Object value) {
            if (value == null) {
                return "";
            }
            if (value instanceof Number) {
                return String.valueOf(value);
            }
            return value;
        }
    };

    class TestVO {

        private Number num;

        private String name;

        public Number getNum() {
            return num;
        }

        public void setNum(Number num) {
            this.num = num;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
