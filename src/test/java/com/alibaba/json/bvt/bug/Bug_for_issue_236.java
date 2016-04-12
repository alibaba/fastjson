package com.alibaba.json.bvt.bug;

import java.util.Map;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import junit.framework.TestCase;

public class Bug_for_issue_236 extends TestCase {

    public void test_for_issue() throws Exception {
        String text = "{1:{\"donateLevel\":0,\"goodsInfoInRoomMap\":{102:2160,103:0},\"goodsInfoMap\":null,\"headPhoto\":null,\"headPhotoId\":0,\"id\":-569,\"nickName\":\"啤酒兑咖啡的苦涩\",\"sex\":1,\"vipLevel\":0},2:{\"donateLevel\":0,\"goodsInfoInRoomMap\":{102:11000,103:0},\"goodsInfoMap\":null,\"headPhoto\":null,\"headPhotoId\":1,\"id\":18315,\"nickName\":\"游客6083\",\"sex\":1,\"vipLevel\":0},3:{\"donateLevel\":0,\"goodsInfoInRoomMap\":{102:1940,103:0},\"goodsInfoMap\":null,\"headPhoto\":null,\"headPhotoId\":0,\"id\":-887,\"nickName\":\"傻笑，那段情\",\"sex\":0,\"vipLevel\":0},5:{\"$ref\":\"$[2]\"}}";
        Map<Integer, Object> root = JSON.parseObject(text, new TypeReference<Map<Integer, Object>>() {});
        Assert.assertNotNull(root.get(5));
    }

    public static class TestPara {

        public Object[] paras;
    }
}
