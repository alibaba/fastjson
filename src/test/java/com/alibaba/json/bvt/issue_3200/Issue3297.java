package com.alibaba.json.bvt.issue_3200;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import junit.framework.TestCase;

import java.io.Serializable;
import java.util.Map;

public class Issue3297 extends TestCase {
    public void test_for_issue() throws Exception {
        JSONObject json = JSON.parseObject("{\"map\":null,\"isHot\":null}");
        System.out.println(JSON.toJavaObject(json, HotTextVO.class));
    }

    public static class HotTextVO implements Serializable {
        @JSONField(name = "map")
        public Map<String, String> map;

        @SerializedName("isHot")
        public int isHot;

        // 不加set方法时也有问题
        public void setIsHot(int isHot) {
            this.isHot = isHot;
        }

        public HotTextVO() {
        }

        @Override
        public String toString() {
            return "HotTextVO{" +
                    ", isHot=" + isHot +
                    '}';
        }
    }
}
