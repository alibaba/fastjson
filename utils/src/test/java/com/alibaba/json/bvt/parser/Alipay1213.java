package com.alibaba.json.bvt.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by wenshao on 13/12/2016.
 */
public class Alipay1213 extends TestCase {
    public void test_for_issue() throws Exception {
        String text = "{\"resultObj\":{\"assetSize\":{},\"chargeTypeList\":[\"A\"],\"fundManagerMapList\":[{},{}],\"fundRateConvertList\":[{},{},{}],\"fundRateOperate\":{},\"fundRatePurchaseList\":[{\"fixedRate\":{},\"maxAmount\":{},\"minAmount\":{}}],\"fundRateRedeemList\":[{\"fixedRate\":{}}],\"fundRateSubscribeList\":[{\"fixedRate\":{},\"maxAmount\":{},\"minAmount\":{}}],\"fundRatingList\":[{},{}],\"fundRuleConvertList\":[{},{}],\"fundRuleConvertVoList\":[{\"fundRateConvertList\":[{\"$ref\":\"$.resultObj.fundRateConvertList[0]\"},{\"$ref\":\"$.resultObj.fundRateConvertList[1]\"}],\"fundRuleConvert\":{\"$ref\":\"$.resultObj.fundRuleConvertList[0]\"}},{\"fundRateConvertList\":[{\"$ref\":\"$.resultObj.fundRateConvertList[2]\"}],\"fundRuleConvert\":{\"$ref\":\"$.resultObj.fundRuleConvertList[1]\"}}]}}";
        JSONObject root = JSON.parseObject(text);

        JSONObject resultObj = root.getJSONObject("resultObj");
        assertNotNull(resultObj);

        JSONArray fundRuleConvertVoList = resultObj.getJSONArray("fundRuleConvertVoList");
        assertNotNull(fundRuleConvertVoList);

        JSONArray fundRateConvertList = fundRuleConvertVoList.getJSONObject(0).getJSONArray("fundRateConvertList");
        assertNotNull(fundRateConvertList);

        assertNotNull(fundRateConvertList.get(0));
    }
}
