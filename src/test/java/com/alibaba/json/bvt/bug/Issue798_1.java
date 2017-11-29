/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;

/**
 * 类Test.java的实现描述：TODO 类实现描述 
 * @author jieyu.ljy 2016年7月22日 下午12:39:17
 */
public class Issue798_1 extends TestCase {
    
    public void test_for_issue() throws Exception {
        String str = "<p>主要学校：密歇根大学 安娜堡分校、东密西根大学、 克莱利学院、康考迪亚学院 、瓦什特洛社区学院</p>";
        String json = JSON.toJSONString(str);
        assertEquals("\"<p>主要学校：密歇根大学 安娜堡分校、东密西根大学、 克莱利学院\\u007F、康考迪亚学院 \\u007F、瓦什特洛社区学院\\u007F</p>\"", json);

        String parsedStr = (String) JSON.parse(json);
        assertEquals(str, parsedStr);
    }
}
