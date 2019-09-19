package com.alibaba.json.bvt.issue_2400;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import junit.framework.TestCase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Issue2428 extends TestCase {
    private String myName;
    private NestedBean nestedBean;

    @AllArgsConstructor
    @Data
    public static class NestedBean {
        private String myId;
    }

    public void test_for_issue() {
        Issue2428 demoBean = new Issue2428();
        demoBean.setMyName("test name");
        demoBean.setNestedBean(new NestedBean("test id"));
        assertEquals("{\"nestedBean\":{\"myId\":\"test id\"},\"myName\":\"test name\"}", JSON.toJSON(demoBean).toString());

        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

        assertEquals("{\"my_name\":\"test name\",\"nested_bean\":{\"my_id\":\"test id\"}}", JSON.toJSON(demoBean, serializeConfig).toString());
    }
}
