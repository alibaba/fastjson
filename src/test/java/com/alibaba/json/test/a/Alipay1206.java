package com.alibaba.json.test.a;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by wenshao on 06/12/2016.
 */
public class Alipay1206 extends TestCase {
    public void test_for_alipay() throws Exception {
        File file = new File("/Users/wenshao/Downloads/x.txt");
        String text = FileUtils.readFileToString(file);
        JSONObject root = JSON.parseObject(text);

        JSONObject resultObj = root.getJSONObject("resultObj");
        assertNotNull(resultObj);

        JSONArray conditionGroupItemList = resultObj.getJSONArray("conditionGroupItemList");
        assertNotNull(conditionGroupItemList);

        JSONObject conditionGroup = conditionGroupItemList.getJSONObject(0).getJSONObject("conditionGroup");
        assertNotNull(conditionGroup);

        JSONArray recordList = conditionGroup.getJSONArray("recordList");
        assertNotNull(recordList);

        JSONArray conditionItemList = recordList.getJSONObject(0).getJSONArray("conditionItemList");
        assertNotNull(conditionItemList);

        JSONObject condition = conditionItemList.getJSONObject(18).getJSONObject("condition");
        assertNotNull(condition);

        JSONArray conditionConstraint = condition.getJSONArray("conditionConstraint");
        assertNotNull(conditionConstraint);

        JSONObject constraintOptionalRecordMap = conditionConstraint.getJSONObject(0).getJSONObject("constraintOptionalRecordMap");
        assertNotNull(constraintOptionalRecordMap);

        System.out.println(constraintOptionalRecordMap);
    }
}
