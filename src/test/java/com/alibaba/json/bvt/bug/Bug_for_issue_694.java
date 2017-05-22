package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class Bug_for_issue_694 extends TestCase {
    public void test_for_issue() throws Exception {
        Root root = JSON.parseObject("{\"entity\":{\"isBootomPluginClickable\":true,\"isMainPoi\":true}}", Root.class);
        Assert.assertTrue(root.entity.isBootomPluginClickable);
        Assert.assertTrue(root.entity.isMainPoi);
    }

    public static class Root {
        public GSMapItemBIZEntity entity;

        class GSMapItemBIZEntity {
            protected boolean isBootomPluginClickable = false; // 金融部门外币兑换业务网点 点击底部无需跳转
            protected boolean isMainPoi               = false;

            public boolean isBootomPluginClickable() {
                return isBootomPluginClickable;
            }

            public void setBootomPluginClickable(boolean bootomPluginClickable) {
                isBootomPluginClickable = bootomPluginClickable;
            }

            public boolean isMainPoi() {
                return isMainPoi;
            }

            public void setMainPoi(boolean mainPoi) {
                isMainPoi = mainPoi;
            }
        }
    }
}
