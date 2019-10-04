package com.alibaba.json.bvt.issue_2700;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  <br>
 *
 * @author byw
 * @create 2019/10/4
 */
public class Issue2750 extends TestCase {
    public void test_01(){
        List list = new ArrayList<Long>();
        list.add(5678901L);
        Map<String, List> param = new HashMap<String, List>();
        param.put("list", list);
        Map<String, List> map = JSON.parseObject(JSON.toJSONString(param), Map.class);
        List listTmp = map.get("list");
        Assert.assertTrue(listTmp.get(0).getClass().toString().equals("class java.lang.Long"));
    }
}
