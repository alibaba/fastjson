package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import net.sf.json.JSONNull;

/**
 * Created by wenshao on 17/01/2017.
 */
public class Issue997 extends TestCase {
    public void test_for_issue() throws Exception {

        Model model = new Model();
        model.object = JSONNull.getInstance();
        System.out.println(JSON.toJSONString(model));
//        System.out.println(JSON.toJSONString(map));
    }

    public static class Model {
        public Object object;
    }
}
