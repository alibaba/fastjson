package com.alibaba.json.bvt.issue_1500;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import junit.framework.TestCase;

import java.io.Serializable;
import java.util.List;

public class Issue1548 extends TestCase {
    public void test_for_issue() throws Exception {
        String msg = "[{\"doc\":{\"bottomprice\":80,\"cashpool_isdeleted\":0,\"shopcityid\":190,\"timerange\":\"2017-10-25;2017-10-26\",\"launchentityid\":3048,\"bidprice\":700,\"targetitems\":\"{}\",\"type\":0,\"slottagid\":44,\"targetid\":330048,\"entity_isdeleted\":0,\"bu\":2,\"target_isdeleted\":0,\"shopid\":6067941,\"slotids\":\"50041,10233,50051,10033,50061,50001,10099,10133,50101,10051\",\"launchscope\":0,\"productid\":74,\"creativeid\":300048,\"dpentitystatus\":1,\"accountid\":20151002,\"entitytype\":4,\"launchplatforms\":\"\",\"iszhuantou\":0,\"dpentityid\":6067941,\"timeslotperiod\":\"0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167\",\"templateid\":23,\"category1\":246,\"launch_isdeleted\":0,\"cashpoolid\":20151002,\"creative_isdeleted\":0,\"settlementstatus\":1,\"cityid\":\"190\",\"planid\":1042007,\"categoryid\":\"10 246\",\"price\":700,\"shoptype\":10,\"plan_isdeleted\":0,\"launchid\":30000048,\"creativeext\":\"{\\\"content\\\":\\\"啊啊啊啊啊啊啊啊\\\",\\\"title\\\":\\\"啊啊啊啊啊\\\",\\\"smartPic\\\":0,\\\"mobUrl\\\":\\\"https://evt.dianping.com/midas/1activities/3809/index.html?dpid=7997757988618737578&cityid=1&longitude=121.41543&latitude=31.21684&token=&product=dpapp&area=pc\\\",\\\"mtMobUrl\\\":\\\"https://evt.dianping.com/midas/1activities/3809/index.html?dpid=7997757988618737578&cityid=1&longitude=121.41543&latitude=31.21684&token=&product=dpapp&area=mtapp\\\"}\",\"chargetype\":1,\"channel\":0,\"generatedchannel\":0,\"promotype\":2},\"meta\":{\"LSN\":2077395,\"AREA\":\"engine-searchcpc\",\"PRIMARY_KEY\":[\"creativeid\",\"targetid\"],\"SECONDARY_KEY\":[\"planid\",\"shopid\",\"launchentityid\",\"launchid\",\"cashpoolid\"],\"TYPE\":\"UPDATE\"}}]";
        // JSONArray.parse(msg);
        JSON.parseArray(msg).toJavaList(PublishDoc.class);
    }

    public static class PublishDoc implements Serializable {

        public static final String LSN_META_NAME = "LSN";
        public static final String DOCTYPE_META_NAME = "TYPE";
        public static final String AREA_META_NAME = "AREA";
        public static final String PRIMARY_KEY_META_NAME = "PRIMARY_KEY";
        public static final String SECONDARY_KEY_META_NAME = "SECONDARY_KEY";

        private JSONObject meta;
        private JSONObject doc;

        public PublishDoc() {
            this.meta = new JSONObject();
            this.doc = new JSONObject();
        }


        @JSONField(serialize = false)
        public void addMeta(String name, Object value) {
            this.meta.put(name, value);
        }

        @JSONField(serialize = false)
        public Object getMeta(String name) {
            return this.meta.get(name);
        }



        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }
    }

    public static enum DocType{

    }
}
