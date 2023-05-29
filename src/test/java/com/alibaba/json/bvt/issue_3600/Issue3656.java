package com.alibaba.json.bvt.issue_3600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @Author ：Nanqi
 * @Date ：Created in 21:43 2021/02/25
 */
public class Issue3656 {
    @Test(expected = JSONException.class)
    public void test_for_issue() {
        String jsonStr = "{" +
                "    \"serviceType\":\"dubbo\"," +
                "    \"types\":[" +
                "        {" +
                "            \"enums\":[" +
                "" +
                "            ]," +
                "            \"typeBuilderName\":\"DefaultTypeBuilder\"," +
                "            \"type\":\"int\"," +
                "            \"items\":[" +
                "" +
                "            ]," +
                "            \"properties\":{" +
                "" +
                "            }" +
                "        }" +
                "    ]" +
                "}";
        Metadata m = JSON.parseObject(jsonStr, Metadata.class);
    }

    public static class Type {
        private List<String> enums;
        private String typeBuilderName;
        private String type;

        public List<String> getEnums() {
            return enums;
        }

        public void setEnums(List<String> enums) {
            this.enums = enums;
        }

        public String getTypeBuilderName() {
            return typeBuilderName;
        }

        public void setTypeBuilderName(String typeBuilderName) {
            this.typeBuilderName = typeBuilderName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class Metadata {
        private String serviceType;
        private Map<String, Type> types;

        public String getServiceType() {
            return serviceType;
        }

        public void setServiceType(String serviceType) {
            this.serviceType = serviceType;
        }

        public Map<String, Type> getTypes() {
            return types;
        }

        public void setTypes(Map<String, Type> types) {
            this.types = types;
        }
    }
}
