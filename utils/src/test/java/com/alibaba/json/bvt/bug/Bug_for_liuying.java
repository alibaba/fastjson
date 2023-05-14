package com.alibaba.json.bvt.bug;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;

/**
 * Created by wenshao on 18/01/2017.
 */
public class Bug_for_liuying extends TestCase {

    public void test_for_bug() throws Exception {
        String aa = "[{\"dictFont\":\"&#xe62b;\",\"dictId\":\"wap\",\"dictName\":\"无线&手淘\"},{\"dictFont\":\"&#xe62a;\",\"dictId\":\"etao\",\"dictName\":\"搜索\"}]";
        JSONObject jsonResult = new JSONObject();
        JSONArray jsonArray = JSONArray.parseArray(aa);
        jsonResult.put("aaa", jsonArray);

        System.out.println(jsonResult);
    }
}
