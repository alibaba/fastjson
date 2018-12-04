package com.alibaba.json.bvt.kotlin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class Issue_for_kotlin_20181203 extends TestCase {
    public void test_user() throws Exception {
        ExtClassLoader classLoader = new ExtClassLoader();
        Class clazz = classLoader.loadClass("com.autonavi.falcon.data.service.vulpeData.ProjectItemCheckItemRelation1");

        String str = "    [{\n"
                + "        \"project_item\": \"1105067\",\n"
                + "        \"project_name\": \"明明想\",\n"
                + "        \"product_id_3\": \"0210202\",\n"
                + "        \"task_type_name\": \"黎明X\",\n"
                + "        \"product_id_2\": \"02102\",\n"
                + "        \"product_id_1\": \"021\",\n"
                + "        \"job_item_type\": \"高中\",\n"
                + "        \"product_name_1\": \"犀利\",\n"
                + "        \"product_name_2\": \"基础路网\",\n"
                + "        \"unit\": \"条\",\n"
                + "        \"product_name_3\": \"到底\",\n"
                + "        \"unitremark\": \"任务条数\",\n"
                + "        \"task_type\": \"57205\"\n"
                + "    }]";

        System.out.println(JSON.VERSION);

        Object obj = JSONArray.parseArray(str, clazz);
        String result = JSON.toJSONString(obj);
        System.out.println(result);
        assertEquals("[{\"job_item_type\":\"高中\",\"product_id_1\":\"021\",\"product_id_2\":\"02102\",\"product_id_3\":\"0210202\",\"product_name_1\":\"犀利\",\"product_name_2\":\"基础路网\",\"product_name_3\":\"到底\",\"project_item\":\"1105067\",\"project_name\":\"明明想\",\"task_type\":\"57205\",\"task_type_name\":\"黎明X\",\"unit\":\"条\",\"unitremark\":\"任务条数\"}]", result);
    }

    private static class ExtClassLoader extends ClassLoader {

        public ExtClassLoader() throws IOException {
            super(Thread.currentThread().getContextClassLoader());

            {
                byte[] bytes;
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("kotlin/ProjectItemCheckItemRelation1.clazz");
                bytes = IOUtils.toByteArray(is);
                is.close();

                super.defineClass("com.autonavi.falcon.data.service.vulpeData.ProjectItemCheckItemRelation1", bytes, 0, bytes.length);
            }
        }
    }
}
