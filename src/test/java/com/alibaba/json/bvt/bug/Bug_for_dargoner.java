package com.alibaba.json.bvt.bug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.alibaba.json.bvtVO.DataTransaction;


public class Bug_for_dargoner extends TestCase {
    public void test_0 () throws Exception {
        DataTransaction dt = new DataTransaction();

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> m = new HashMap<String, Object>();
        m.put("name", "tom");
        m.put("sex", "m");
        list.add(m);

        dt.setDataSet("1000", list);
        dt.setRetMsgCode("1", "ok");
        dt.getHead().setAppid("back");
        dt.getHead().setSeqno("201010");
        dt.getHead().getUser().setId("root");

        Map<String, String> m2 = new HashMap<String, String>();
        m2.put("name1", "tom");
        m2.put("name2", "tom");
        m2.put("name3", "tom");

        dt.getBody().getParam().setForm(m2);

        System.out.println(dt.toJSON());

        DataTransaction dt2 = DataTransaction.fromJSON(dt.toJSON());
        System.out.println(dt2.toJSON());
    }
}
