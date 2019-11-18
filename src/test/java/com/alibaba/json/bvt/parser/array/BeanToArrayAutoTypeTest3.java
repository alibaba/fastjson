package com.alibaba.json.bvt.parser.array;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

import java.util.List;

public class BeanToArrayAutoTypeTest3
        extends TestCase {
    public void test_beanToArray() throws Exception {
        Topology topology = JSON.parseObject("{\"maps\":[[\"@type\":\"Log\"]]}", Topology.class);
        assertEquals(LogSourceMeta.class, topology.maps.get(0).getClass());
    }

    public void test_beanToArray1() throws Exception {
        Topology topology = JSON.parseObject("{\"maps\":[[\"@type\":\"Log\",123]]}", Topology.class);
        assertEquals(LogSourceMeta.class, topology.maps.get(0).getClass());
        assertEquals(123, ((LogSourceMeta) topology.maps.get(0)).id);
    }

    @JSONType(typeName = "Log")
    public static class LogSourceMeta extends MapTaskMeta {
        public int id;
    }

    @JSONType(seeAlso = {LogSourceMeta.class, OtherMeta.class}, parseFeatures = Feature.SupportArrayToBean)
    public static class MapTaskMeta {

    }

    @JSONType(typeName = "Other")
    public static class OtherMeta extends MapTaskMeta {

    }

    public static class Topology {
        public List<MapTaskMeta> maps;
    }
}
