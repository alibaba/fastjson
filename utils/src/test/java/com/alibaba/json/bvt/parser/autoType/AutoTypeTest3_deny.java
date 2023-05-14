package com.alibaba.json.bvt.parser.autoType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import junit.framework.TestCase;

public class AutoTypeTest3_deny extends TestCase {
    public void test_for_x() throws Exception {
        Exception error = null;
        try {
            String json = "{\"@type\":\"java.util.logging.FileHandler\",\"pattern\":\"xxx.txt\"}";
            Object obj = JSON.parse(json, Feature.SupportAutoType);
        } catch (Exception ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_for_jar() throws Exception {
        Exception error = null;
        try {
            String json = "{\"@type\":\"java.util.jar.JarFile.JarFile\",\"name\":\"xxx.txt\"}";
            Object obj = JSON.parse(json, Feature.SupportAutoType);
        } catch (Exception ex) {
            error = ex;
        }
        assertNotNull(error);
    }

    public void test_for_array() throws Exception {
        Exception error = null;
        try {
            String json = "{\"@type\":\"[java.util.jar.JarFile.JarFile\",\"name\":\"xxx.txt\"}";
            Object obj = JSON.parse(json, Feature.SupportAutoType);
        } catch (Exception ex) {
            ex.printStackTrace();
            error = ex;
        }
        assertNotNull(error);
    }


    public void test_for_array_1() throws Exception {
        Exception error = null;
        try {
            String json = "{\"@type\":\"L[java.util.jar.JarFile.JarFile;\",\"name\":\"xxx.txt\"}";
            Object obj = JSON.parse(json, Feature.SupportAutoType);
        } catch (Exception ex) {
            ex.printStackTrace();
            error = ex;
        }
        assertNotNull(error);
    }
}
