package com.alibaba.json.bvt.issue_3400;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;
import lombok.Data;
import org.junit.Assert;

/**
 * Description:  <br>
 *
 * @author byw
 * @create 2020/9/19
 */
public class Issue3452 extends TestCase {

    public void test_for_issue() throws Exception {
        String s = "{ \"componentKey\" : \"CMDB_UPDATE_SERVER\"}";
        Step step = JSON.parseObject(s, Step.class);
        Assert.assertEquals("CMDB_UPDATE_SERVER",step.getComponentKey());
        System.out.println(step.getComponentKey());
    }


    private static class Step {
        @JSONField(name = "component_key")
        private String componentKey;

        public String getComponentKey() {
            return componentKey;
        }

        public void setComponentKey(String componentKey) {
            this.componentKey = componentKey;
        }
    }
}
