package com.alibaba.fastjson.deserializer.issue4129;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.deserializer.issue4129.beans.Campaign;
import com.alibaba.fastjson.deserializer.issue4129.beans.Policy;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Calendar;

public class TestIssue4129 {

    @Test
    public void test_for_issue4129_0() {
        Policy policy = new Policy();
        policy.setStartTime(Calendar.getInstance().getTime());
        policy.setEndTime(Calendar.getInstance().getTime());
        String policyJson = JSONObject.toJSONString(policy);

        Policy policyCopy = JSONObject.parseObject(policyJson).toJavaObject(Policy.class);
        String policyJsonCopy = JSONObject.toJSONString(policyCopy);

        Assert.assertEquals(policyJson, policyJsonCopy);
    }

    @Test
    public void test_for_issue4129_1() {
        Campaign campaign = new Campaign();
        campaign.setStartTime(LocalDateTime.now());
        campaign.setEndTime(LocalDateTime.now());
        String campaignJson = JSONObject.toJSONString(campaign);

        Campaign campaignCopy = JSONObject.parseObject(campaignJson).toJavaObject(Campaign.class);
        String campaignJsonCopy = JSONObject.toJSONString(campaignCopy);

        Assert.assertEquals(campaignJson, campaignJsonCopy);
    }

}
