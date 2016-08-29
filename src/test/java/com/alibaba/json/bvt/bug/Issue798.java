package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * Created by wenshao on 16/8/29.
 */
public class Issue798 extends TestCase {
    public void test_for_issue() throws Exception {
        Model model = new Model();
        model.value = " 主要学校：密歇根大学 安娜堡分校、东密西根大学、 克莱利学院�、康考迪亚学院 �、瓦什特洛社区学院� ";
        String json = JSON.toJSONString(model);

        Model model2 = JSON.parseObject(json, Model.class);
        assertEquals(model.value, model2.value);
    }

    public static class Model {
        public String value;
    }

}
