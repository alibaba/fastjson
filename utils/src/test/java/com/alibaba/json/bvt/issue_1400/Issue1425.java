package com.alibaba.json.bvt.issue_1400;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

public class Issue1425 extends TestCase {
    public void test_for_issue() throws Exception {
        DicDomain dicDomain = new DicDomain();
        dicDomain.setCode("A001");
        dicDomain.setName("测试");

        SerializerFeature[] features = new SerializerFeature[]{
                SerializerFeature.NotWriteRootClassName,
                SerializerFeature.WriteClassName,
                SerializerFeature.DisableCircularReferenceDetect
        };

        System.out.println(JSON.toJSONString(dicDomain, features));
    }
    public static class DicDomain {

        private String code;

        private String name;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
