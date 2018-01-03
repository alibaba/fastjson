package com.alibaba.json.bvt.support.jaxrs;

import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.jaxrs.FastJsonProvider;
import junit.framework.TestCase;
import org.junit.Assert;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class FastJsonProviderTest extends TestCase {

    @SuppressWarnings("deprecation")
    public void test_1() throws Exception {

        FastJsonProvider provider1 = new FastJsonProvider("UTF-8");
        Assert.assertEquals("UTF-8", provider1.getCharset().name());

        FastJsonProvider provider2 = new FastJsonProvider();

        provider2.setCharset(Charset.forName("GBK"));
        Assert.assertEquals("GBK", provider2.getCharset().name());

        Assert.assertNull(provider2.getDateFormat());
        provider2.setDateFormat("yyyyMMdd");

        provider2.setFeatures(SerializerFeature.IgnoreErrorGetter);
        Assert.assertEquals(1, provider2.getFeatures().length);
        Assert.assertEquals(SerializerFeature.IgnoreErrorGetter,
                provider2.getFeatures()[0]);

        provider2.setFilters(serializeFilter);
        Assert.assertEquals(1, provider2.getFilters().length);
        Assert.assertEquals(serializeFilter, provider2.getFilters()[0]);

        FastJsonProvider provider = new FastJsonProvider(new Class[]{VO.class});

        Assert.assertNotNull(provider.getFastJsonConfig());

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setWriteContentLength(false);
        provider.setFastJsonConfig(fastJsonConfig);

        Assert.assertEquals(true, provider.isReadable(VO.class, VO.class, null, MediaType.APPLICATION_JSON_TYPE));
        Assert.assertEquals(true, provider.isWriteable(VO.class, VO.class, null, MediaType.APPLICATION_JSON_TYPE));
        Assert.assertEquals(true, provider.isReadable(VO.class, VO.class, null, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
        Assert.assertEquals(true, provider.isWriteable(VO.class, VO.class, null, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
        Assert.assertEquals(false, provider.isReadable(VO.class, VO.class, null, MediaType.APPLICATION_XML_TYPE));
        Assert.assertEquals(false, provider.isWriteable(VO.class, VO.class, null, MediaType.APPLICATION_XML_TYPE));
        Assert.assertEquals(false, provider.isReadable(String.class, String.class, null, MediaType.valueOf("application/javascript")));
        Assert.assertEquals(false, provider.isWriteable(String.class, String.class, null, MediaType.valueOf("application/x-javascript")));
        Assert.assertEquals(false, provider.isReadable(String.class, String.class, null, MediaType.valueOf("applications/+json")));
        Assert.assertEquals(false, provider.isWriteable(String.class, String.class, null, MediaType.valueOf("applications/x-json")));
        Assert.assertEquals(false, provider.isReadable(null, null, null, MediaType.valueOf("application/x-javascript")));
        Assert.assertEquals(false, provider.isWriteable(null, null, null, null));
        Assert.assertEquals(false, provider.isReadable(InputStream.class, null, null, MediaType.valueOf("application/x-javascript")));
        Assert.assertEquals(false, provider.isWriteable(OutputStream.class, null, null, null));


        VO vo = (VO) provider.readFrom(null, VO.class, null, MediaType.APPLICATION_JSON_TYPE, null, new ByteArrayInputStream("{\"id\":123}".getBytes(Charset
                .forName("UTF-8"))));
        Assert.assertEquals(123, vo.getId());

        final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        provider.writeTo(vo, VO.class, VO.class, null, MediaType.APPLICATION_JSON_TYPE, new MultivaluedHashMap<String, Object>(), byteOut);

        byte[] bytes = byteOut.toByteArray();
        Assert.assertEquals("{\"id\":123}", new String(bytes, "UTF-8"));

        provider.getSize(vo, VO.class, VO.class, null, MediaType.APPLICATION_JSON_TYPE);

        try {
            provider.readFrom(null, VO.class, null, MediaType.APPLICATION_JSON_TYPE, null, new ByteArrayInputStream("\"id\":123".getBytes(Charset
                    .forName("UTF-8"))));
        } catch (WebApplicationException ex) {
            Assert.assertNotNull(ex);
        }
    }

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

    public static class VO {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }
}
