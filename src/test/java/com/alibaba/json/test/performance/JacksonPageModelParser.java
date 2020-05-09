package com.alibaba.json.test.performance;

import static com.alibaba.json.test.performance.JacksonPageModelParser.JsonParserHelper.assertExpectedFiled;
import static com.alibaba.json.test.performance.JacksonPageModelParser.JsonParserHelper.assertExpectedJsonToken;
import static com.alibaba.json.test.performance.JacksonPageModelParser.JsonParserHelper.getNextTextValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.json.test.entity.pagemodel.LayoutInstance;
import com.alibaba.json.test.entity.pagemodel.PageInstance;
import com.alibaba.json.test.entity.pagemodel.RegionEnum;
import com.alibaba.json.test.entity.pagemodel.RegionInstance;
import com.alibaba.json.test.entity.pagemodel.SegmentInstance;
import com.alibaba.json.test.entity.pagemodel.WidgetInstance;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JacksonPageModelParser {

    /**
     * @param content
     * @throws JsonParseException
     * @throws IOException
     */
    public PageInstance parse(String content) throws JsonParseException, IOException {
        JsonFactory f = new JsonFactory();
        JsonParser parser = f.createJsonParser(content);

        JsonToken current = parser.nextToken(); // move to the start of the
        // object

        String instanceId = getNextTextValue("sid", parser); // get instanceId

        String pageId = getNextTextValue("cid", parser); // get pageId

        current = parser.nextToken(); // move to field: segments
        assertExpectedFiled(parser.getCurrentName(), "segments", parser.getCurrentLocation());

        PageInstance pageInstance = new PageInstance();
        pageInstance.setCid(pageId);
        pageInstance.setSid(Long.valueOf(instanceId));
        pageInstance.setSegments(parseSegments(parser));
        return pageInstance;
        // 构建组件树，用于递归渲染
        // pageInstance.buildComponentTree();

    }

    /**
     * @param parser
     * @throws JsonParseException
     * @throws IOException
     */
    private List<SegmentInstance> parseSegments(JsonParser parser) throws JsonParseException, IOException {
        JsonToken current = parser.nextToken();

        assertExpectedJsonToken(current, JsonToken.START_ARRAY, parser.getCurrentLocation());
        List<SegmentInstance> instances = new ArrayList<SegmentInstance>();
        while ((current = parser.nextToken()) != JsonToken.END_ARRAY) {

            assertExpectedJsonToken(current, JsonToken.START_OBJECT, parser.getCurrentLocation());

            String segmentId = getNextTextValue("cid", parser); // get pageId

            current = parser.nextToken(); // move to field: layouts
            assertExpectedFiled(parser.getCurrentName(), "layouts", parser.getCurrentLocation());
            SegmentInstance instance = new SegmentInstance();
            instance.setLayouts(parseLayouts(parser, segmentId));
            instances.add(instance);

            assertExpectedJsonToken((current = parser.nextToken()), JsonToken.END_OBJECT, parser.getCurrentLocation());

        }
        return instances;

    }

    /**
     * @param parser
     * @param segmentId
     * @throws JsonParseException
     * @throws IOException
     */
    private List<LayoutInstance> parseLayouts(JsonParser parser, String segment) throws JsonParseException, IOException {

        JsonToken current = parser.nextToken();

        assertExpectedJsonToken(current, JsonToken.START_ARRAY, parser.getCurrentLocation());

        List<LayoutInstance> layoutInThisSegment = new ArrayList<LayoutInstance>();

        while ((current = parser.nextToken()) != JsonToken.END_ARRAY) {

            assertExpectedJsonToken(current, JsonToken.START_OBJECT, parser.getCurrentLocation());

            String instanceId = getNextTextValue("sid", parser); // get
                                                                 // instanceId
            String layoutId = getNextTextValue("cid", parser); // get
                                                               // layoutId

            LayoutInstance layoutInstance = new LayoutInstance();
            layoutInstance.setCid(layoutId);
            layoutInstance.setSid(Long.valueOf(instanceId));
            layoutInThisSegment.add(layoutInstance);

            current = parser.nextToken(); // move to field: regions
            assertExpectedFiled(parser.getCurrentName(), "regions", parser.getCurrentLocation());

            layoutInstance.setRegions(parseRegions(parser, segment));

            assertExpectedJsonToken((current = parser.nextToken()), JsonToken.END_OBJECT, parser.getCurrentLocation());
        }
        return layoutInThisSegment;

    }

    /**
     * @param parser
     * @param layoutId
     * @throws JsonParseException
     * @throws IOException
     */
    private List<RegionInstance> parseRegions(JsonParser parser, String segment) throws JsonParseException, IOException {

        JsonToken current = parser.nextToken();

        assertExpectedJsonToken(current, JsonToken.START_ARRAY, parser.getCurrentLocation());
        List<RegionInstance> instances = new ArrayList<RegionInstance>();
        while ((current = parser.nextToken()) != JsonToken.END_ARRAY) {

            assertExpectedJsonToken(current, JsonToken.START_OBJECT, parser.getCurrentLocation());

            String regionId = getNextTextValue("cid", parser); // get regionId
            RegionEnum region = RegionEnum.valueOf(regionId);

            current = parser.nextToken(); // move to field: widgtes
            assertExpectedFiled(parser.getCurrentName(), "widgets", parser.getCurrentLocation());
            RegionInstance instance = new RegionInstance();
            instance.setWidgtes(parseWidgets(parser, region));
            instances.add(instance);

            assertExpectedJsonToken((current = parser.nextToken()), JsonToken.END_OBJECT, parser.getCurrentLocation());
        }
        return instances;

    }

    /**
     * @param parser
     * @param layoutId
     * @param regionId
     * @throws JsonParseException
     * @throws IOException
     */
    private List<WidgetInstance> parseWidgets(JsonParser parser, RegionEnum region) throws JsonParseException, IOException {

        JsonToken current = parser.nextToken();

        assertExpectedJsonToken(current, JsonToken.START_ARRAY, parser.getCurrentLocation());

        List<WidgetInstance> widgetInThisRegion = new ArrayList<WidgetInstance>();

        while ((current = parser.nextToken()) != JsonToken.END_ARRAY) {

            assertExpectedJsonToken(current, JsonToken.START_OBJECT, parser.getCurrentLocation());

            String instanceId = getNextTextValue("sid", parser); // get
                                                                 // instanceId
            String widgetId = getNextTextValue("cid", parser); // get
                                                               // widgetId

            WidgetInstance widgetInstance = new WidgetInstance();
            widgetInstance.setCid(widgetId);
            widgetInstance.setSid(Long.valueOf(instanceId));

            widgetInThisRegion.add(widgetInstance);

            assertExpectedJsonToken((current = parser.nextToken()), JsonToken.END_OBJECT, parser.getCurrentLocation());
        }
        return widgetInThisRegion;

    }

    public static class JsonParserHelper {

        public static String getNextTextValue(String fieldName, JsonParser parser) throws JsonParseException, IOException {

            JsonToken current = parser.nextToken(); // move to filed
            if (current != JsonToken.FIELD_NAME || !fieldName.equals(parser.getCurrentName())) {
                reportParseError("Error occoured while getting value by field name:" + fieldName, parser.getCurrentLocation());
            }
            current = parser.nextToken(); // move to value
            return parser.getText();

        }

        public static void assertExpectedJsonToken(JsonToken object, JsonToken expected, JsonLocation jsonLoc) throws JsonParseException {
            if (object != expected) {
                reportParseError(buildFailMessage(object, expected), jsonLoc);
            }
        }

        public static void assertExpectedFiled(String object, String expected, JsonLocation jsonLoc) throws JsonParseException {
            if (!expected.equals(object)) {
                reportParseError(buildFailMessage(object, expected), jsonLoc);
            }
        }

        private static String buildFailMessage(Object object, Object expected) {
            StringBuilder sb = new StringBuilder();
            sb.append("get [").append(object).append("] but expect [").append(expected).append("] !");
            return sb.toString();
        }

        public static void reportParseError(String errorMsg, JsonLocation jsonLoc) throws JsonParseException {

            throw new JsonParseException(errorMsg, jsonLoc);

        }

    }
}
