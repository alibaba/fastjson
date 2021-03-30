package com.alibaba.fastjson.serializer.issue3666;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.issue3666.beans.Ordered3666;
import com.alibaba.fastjson.serializer.issue3666.beans.Unordered3666;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * https://github.com/alibaba/fastjson/issues/3666
 * @author oyhp
 * @since 2021-03-30 11:40
 */
public class Issue3666 {
    private static final String JSON_FILE_RELATIVE_PATH = "\\src\\test\\resources\\json\\issue3666\\";
    private static final String JSON_FILE_FULL_PATH = System.getProperty("user.dir") + JSON_FILE_RELATIVE_PATH;
    private static final String JSON_ROUTER_FILE_NAME = "router.json";
    private static final String JSON_ROUTER_FULL_PATH_NAME = JSON_FILE_FULL_PATH + JSON_ROUTER_FILE_NAME;
    private static final Logger LOGGER = LoggerFactory.getLogger(Issue3666.class);

    private static void jsonObject2File(JSONObject jsonObject) {
        try {
            OutputStream os = new FileOutputStream(Issue3666.JSON_ROUTER_FULL_PATH_NAME);
            os.write(JSON.toJSONString(jsonObject, true).getBytes(StandardCharsets.UTF_8));
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(Issue3666.JSON_ROUTER_FULL_PATH_NAME + "File reading exception" + e);
        }
    }

    @Test
    public void test_JSONObject_toJSON_ordered() {
        Ordered3666 router = new Ordered3666();
        router.setRouterDesc("router1");
        router.setFlowIp("1.1.1.1");
        router.setFlowPort("9999");
        router.setFlowSampleRate("5000");

        JSONObject json = (JSONObject) JSONObject.toJSON(router);

        String orderJson = "{\"flowSampleRate\":\"5000\",\"flowPort\":\"9999\",\"flowIp\":\"1.1.1.1\",\"routerDesc\":\"router1\"}";
        Assert.assertEquals(orderJson, json.toJSONString());
        jsonObject2File(json);
    }

    @Test
    public void test_JSONObject_toJSON_Unordered() {
        Unordered3666 router = new Unordered3666();
        router.setRouterDesc("router1");
        router.setFlowIp("1.1.1.1");
        router.setFlowPort("9999");
        router.setFlowSampleRate("5000");

        JSONObject json = (JSONObject) JSONObject.toJSON(router);

        String orderJson = "{\"flowIp\":\"1.1.1.1\",\"routerDesc\":\"router1\",\"flowPort\":\"9999\",\"flowSampleRate\":\"5000\"}";
        Assert.assertEquals(orderJson, json.toJSONString());
        jsonObject2File(json);
    }

}
