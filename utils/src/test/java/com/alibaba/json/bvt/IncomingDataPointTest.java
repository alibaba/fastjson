package com.alibaba.json.bvt;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.bvtVO.IncomingDataPoint;
import junit.framework.TestCase;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by wenshao on 03/08/2017.
 */
public class IncomingDataPointTest extends TestCase {
    public void test_0() throws Exception {
        Map<String, String> tags = new LinkedHashMap<String, String>();
        tags.put("site", "et2");
        tags.put("appname", "histore");
        tags.put("ip", "1.1.1.1");

        IncomingDataPoint point = new IncomingDataPoint();
        point.setMetric("mem.usage.GB");
        point.setTimestamp(1501760861298L);
        point.setTags(tags);
        point.setValue("58.41");
        point.setTSUID("");
        point.setAggregator("");
        IncomingDataPoint[] array = new IncomingDataPoint[] {point};

        String json = JSON.toJSONString(array);
        System.out.println(json);

        JSON.parseArray(json, IncomingDataPoint.class);

        IncomingDataPoint p2 = JSON.parseObject("[\"mem.usage.GB\",1501833776283,\"58.41\",{\"site\":\"et2\",\"appname\":\"histore\",\"ip\":\"1.1.1.1\"}]", IncomingDataPoint.class);
        IncomingDataPoint p3 = JSON.parseObject("[\"mem.usage.GB\",1501833776283,\"58.41\",{\"site\":\"et2\",\"appname\":\"histore\",\"ip\":\"1.1.1.1\"},null]", IncomingDataPoint.class);
        System.out.println(JSON.toJSONString(p2));
//        JSON.parseObject(json, IncomingDataPoint[].class);
    }
    public void test_for_IncomingDataPoint() throws Exception {
        // "metric", "timestamp", "value", "tags", "tsuid", "granularity", "aggregator"
        String text = "[[\"DataAdaptor.LbMultiGroupPersonalityDataAdaptor.stddev.aggregate_sum\",\"1501812639932\",\"95.52667633256902\",{\"appName\":\"aladdin\",\"hostIdc\":\"et2\",\"hostunit\":\"CENTER\",\"nodegroup\":\"aladdin_prehost\",\"idc\":\"ET2\",\"agg_version\":\"100\",\"group\":\"DEFAULT\"},\"\",\"\",\"\"]]";
        System.out.println(text);
        JSON.parseArray(text, IncomingDataPoint.class);
    }
}
