package com.alibaba.json.bvt.issue_1200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import junit.framework.TestCase;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by wenshao on 30/05/2017.
 */
public class Issue1233 extends TestCase {
    public void test_for_issue() throws Exception {
        ParserConfig.getGlobalInstance().putDeserializer(Area.class, new ObjectDeserializer() {
            public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
                JSONObject jsonObject = (JSONObject) parser.parse();
                String areaType;

                if (jsonObject.get("type") instanceof String) {
                    areaType = (String) jsonObject.get("type");
                } else {
                    return null;
                }
                if (Area.TYPE_SECTION.equals(areaType)) {
                    return (T) JSON.toJavaObject(jsonObject, Section.class);
                } else if (Area.TYPE_FLOORV1.equals(areaType)) {
                    return (T) JSON.toJavaObject(jsonObject, FloorV1.class);
                } else if (Area.TYPE_FLOORV2.equals(areaType)) {
                    return (T) JSON.toJavaObject(jsonObject, FloorV2.class);
                }
                return null;
            }

            public int getFastMatchToken() {
                return 0;
            }
        });

        JSONObject jsonObject = JSON.parseObject("{\"type\":\"floorV2\",\"templateId\":\"x123\"}");

        FloorV2 floorV2 = (FloorV2) jsonObject.toJavaObject(Area.class);
        assertNotNull(floorV2);
        assertEquals("x123", floorV2.templateId);
    }

    public interface Area {
        public static final String TYPE_SECTION = "section";
        public static final String TYPE_FLOORV1 = "floorV1";
        public static final String TYPE_FLOORV2 = "floorV2";

        String getName();
    }

    public static class Section implements Area {
        public List<Area> children;

        public String type;

        public String templateId;

        public String getName() {
            return templateId;
        }
    }

    public static class FloorV1 implements Area {
        public String type;
        public String templateId;

        public String getName() {
            return templateId;
        }
    }

    public static class FloorV2 implements Area {
        public List<Area> children;

        public String type;

        public String templateId;

        public String getName() {
            return templateId;
        }
    }
}
