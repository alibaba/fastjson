package com.alibaba.json.test;

import java.lang.management.ManagementFactory;
import java.util.Set;

import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.openmbean.SimpleType;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class CompositeDataTest extends TestCase {

    public void test_0() throws Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        // Object value = server.getAttribute(new ObjectName("java.lang:type=Memory"), "HeapMemoryUsage");
        Object value = server.getAttribute(new ObjectName("com.sun.management:type=HotSpotDiagnostic"), "DiagnosticOptions");
        // Object value = server.getAttribute(new ObjectName("java.lang:type=GarbageCollector,name=ParNew"),
        // "CollectionCount");

        Object json = JSON.toJSON(value);
        System.out.println(json);

        Set<ObjectInstance> instances = server.queryMBeans(null, null);
        System.out.println(JSON.toJSONString(instances));

        for (ObjectInstance instance : instances) {
            MBeanInfo mbeanInfo = server.getMBeanInfo(instance.getObjectName());
            System.out.println(JSON.toJSONString(mbeanInfo));
        }

        System.out.println(JSON.toJSONString(SimpleType.BOOLEAN));
    }
    
    public void test_1() throws Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        // Object value = server.getAttribute(new ObjectName("java.lang:type=Memory"), "HeapMemoryUsage");
        Object value = server.getAttribute(new ObjectName("com.sun.management:type=HotSpotDiagnostic"), "DiagnosticOptions");
        // Object value = server.getAttribute(new ObjectName("java.lang:type=GarbageCollector,name=ParNew"),
        // "CollectionCount");

        Object json = JSON.toJSON(value);
        System.out.println(json);

        Set<ObjectInstance> instances = server.queryMBeans(null, null);
        System.out.println(JSON.toJSONString(instances, SerializerFeature.WriteMapNullValue));

        for (ObjectInstance instance : instances) {
            MBeanInfo mbeanInfo = server.getMBeanInfo(instance.getObjectName());
            System.out.println(JSON.toJSONString(mbeanInfo, SerializerFeature.WriteMapNullValue));
        }

        System.out.println(JSON.toJSONString(SimpleType.BOOLEAN, SerializerFeature.WriteMapNullValue));
    }
}
