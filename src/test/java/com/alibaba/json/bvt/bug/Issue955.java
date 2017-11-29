package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONPOJOBuilder;
import com.alibaba.fastjson.annotation.JSONType;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wenshao on 19/12/2016.
 */
public class Issue955 extends TestCase {

    public void test_checkObject() {
        Art origin = makeOrigin();
        JSONObject articleObj = (JSONObject) JSON.toJSON(origin);

        JSONObject dataObj = new JSONObject();
        dataObj.put("art", articleObj);

        Art other = dataObj.getObject("art", Art.class);// return null;
        assertSame(origin, other); // test failed
    }

    public void test_checkArray() throws Exception {
        Art origin = makeOrigin();
        JSONObject object = (JSONObject) JSON.toJSON(origin);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(object);

        Art other = JSON.parseObject(jsonArray.getString(0), Art.class);
        assertSame(origin, other); // test passed

        other = jsonArray.getObject(0, Art.class); // return = null;
        assertSame(origin, other); // test failed
    }

    private Art makeOrigin() {
        final long unixTime = System.currentTimeMillis() / 1000;
        final Art origin = new Art();
        origin.id = "12";
        origin.date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(unixTime * 1000));
        origin.isSupported = true;
        return origin;
    }

    public void assertSame(Art origin, Art another) {
        assertNotNull(another);
        assertEquals(origin.id, another.id);
        assertEquals(origin.date, another.date);
        assertSame(origin.isSupported, another.isSupported);
    }

    @JSONType(builder = Art.Builder.class)
    public static class Art {
        private String id;
        private String date;
        private boolean isSupported;

        public String getId() {
            return id;
        }

        public long getDatetime() throws ParseException {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return (format.parse(date)).getTime() / 1000;
        }

        @JSONField(name = "isSupported")
        public int isSupported() {
            return isSupported ? 1 : 0;
        }

        @JSONPOJOBuilder()
        public final static class Builder {
            private final Art article = new Art();

            public Builder(){

            }

            @JSONField(name = "id")
            public Builder withId(String id) {
                article.id = id;
                return this;
            }

            @JSONField(name = "datetime")
            public Builder withDateTime(long dateTime) {
                if (dateTime > 0)
                    article.date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(dateTime * 1000));
                return this;

            }

            @JSONField(name = "isSupported")
            public Builder withSupported(int supported) {
                article.isSupported = supported == 1;
                return this;
            }

            public Art build() {
                return article;
            }
        }
    }
}
