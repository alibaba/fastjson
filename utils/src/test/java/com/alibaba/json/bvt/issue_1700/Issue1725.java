package com.alibaba.json.bvt.issue_1700;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class Issue1725 extends TestCase {
    public void test_for_issue() throws Exception {
        Map<String, Object> map= new HashMap<String, Object>();
        map.put("enumField", 0);

        AbstractBean bean = JSON.parseObject(JSON.toJSONString(map), ConcreteBean.class);
        assertEquals(FieldEnum.A, bean.enumField);
    }

    public static class AbstractBean {
        public FieldEnum enumField;
    }

    public static class ConcreteBean extends AbstractBean {

    }

    public static enum FieldEnum { A, B }
}
