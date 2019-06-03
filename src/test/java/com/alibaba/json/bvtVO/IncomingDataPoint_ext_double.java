package com.alibaba.json.bvtVO;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessable;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenshao on 03/08/2017.
 */
@JSONType(serialzeFeatures= SerializerFeature.BeanToArray,
        parseFeatures= Feature.SupportArrayToBean,
        orders = {"metric", "timestamp", "value", "tags", "tsuid", "granularity", "aggregator"},
        asm = true
        )
public class IncomingDataPoint_ext_double {
    /** The incoming metric name */
    private String metric;

    /** The incoming timestamp in Unix epoch seconds or milliseconds */
    private long timestamp;

    /** The incoming value as a string, we'll parse it to float or int later */
    private double value;

    /** A hash map of tag name/values */
    private Tags tags;

    /** TSUID for the data point */
    private String tsuid;

    private String granularity;

    private String aggregator;

    /**
     * Empty constructor necessary for some de/serializers
     */
    public IncomingDataPoint_ext_double() {

    }

    /**
     * Constructor used when working with tsuids
     * @param tsuid The TSUID
     * @param timestamp The Unix epoch timestamp
     * @param value The value as a string
     */
    public IncomingDataPoint_ext_double(final String tsuid,
                                        final String granularity,
                                        final long timestamp,
                                        final double value) {
        this.tsuid = tsuid;
        this.granularity = granularity;
        this.timestamp = timestamp;
        this.value = value;
    }



    /** @return the metric */
    public final String getMetric() {
        return metric;
    }

    /** @return the timestamp */
    public final long getTimestamp() {
        return timestamp;
    }

    /** @return the value */
    public final double getValue() {
        return value;
    }

    /** @return the tags */
    public final Tags getTags() {
        return tags;
    }

    /** @return the TSUID */
    @JSONField(name = "tsuid")
    public final String getTSUID() {
        return tsuid;
    }

    public final String getGranularity() {
        return granularity;
    }

    public final String getAggregator() {
        return aggregator;
    }

    public final void setGranularity(String granularity) {
        this.granularity = granularity;
    }

    public final void setAggregator(String aggregator) {
        this.aggregator = aggregator;
    }

    /** @param metric the metric to set */
    public final void setMetric(String metric) {
        this.metric = metric;
    }

    /** @param timestamp the timestamp to set */
    public final void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /** @param value the value to set */
    public final void setValue(double value) {
        this.value = value;
    }

    /** @param tags the tags to set */
    public final void setTags(Tags tags) {
        this.tags = tags;
    }

    /** @param tsuid the TSUID to set */
    @JSONField(name = "tsuid")
    public final void setTSUID(String tsuid) {
        this.tsuid = tsuid;
    }

    @JSONType(orders = {"appName", "hostIdc", "hostunit", "nodegroup", "idc", "hostIp", "agg_version", "group"})
    public static class Tags {
        // 常用字段
        public String appName;
        public String hostIdc;
        public String hostunit;
        public String nodegroup;
        public String idc;
        public String agg_version;
        public String hostIp;
        public String group;

        // 非常用的tag会放在这里，由于标识了unwrapped，序列化时会展开和上面字段并列，反序列化同样能识别
        @JSONField(unwrapped = true)
        public Map<String, String> attributes = new HashMap<String, String>();
    }
}