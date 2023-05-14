package com.alibaba.json.bvt.parser.bug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import junit.framework.TestCase;

import java.util.List;

public class Bug_for_lingzhi extends TestCase {
    public void test_0() throws Exception {
        String str = "[\n" +
                "{\n" +
                "\"isDefault\":false,\n" +
                "\"msgId\": \"expireTransitionChange\",\n" +
                "\"msgText\": \"xxx\",\n" +
                "\"extMsgId\": \"promptInformation\",\n" +
                "\"extMsgText\": \"xxx\",\n" +
                "\"instChangeType\": 1,\n" +
                "\"rule\": {\n" +
                "\"aliUid\":[39314],\n" +
                "\"regionNo\":[]\n" +
                "}\n" +
                "},\n" +
                "{\n" +
                "\"isDefault\":true,\n" +
                "\"msgId\": \"expireTransitionUnChange\",\n" +
                "\"msgText\": \"xxx\",\n" +
                "\"extMsgId\": \"Prompt information\",\n" +
                "\"extMsgText\": \"xxx\",\n" +
                "\"instChangeType\": 0,\n" +
                "\"rule\": {\n" +
                "\"aliUid\":[],\n" +
                "\"regionNo\":[]\n" +
                "}\n" +
                "},\n" +
                "{\n" +
                "\"isDefault\":false,\n" +
                "\"msgId\": \"expireTransitionChange\",\n" +
                "\"msgText\": \"xxx\",\n" +
                "\"extMsgId\": \"Prompt information\",\n" +
                "\"extMsgText\": \"你好B\",\n" +
                "\"instChangeType\": 1,\n" +
                "\"rule\": {\n" +
                "\"aliUid\":[111],\n" +
                "\"regionNo\":[]\n" +
                "}\n" +
                "}\n" +
                "]";

//        String pstr = JSON.toJSONString(JSON.parse(str), SerializerFeature.PrettyFormat);
//        System.out.println(pstr);

        JSON.parseObject(str, new TypeReference<List<EcsTransitionDisplayedMsgConfig>>(){});
    }

    public static class EcsTransitionDisplayedMsgConfig {

        /**
         * 是否默认文案
         */
        private Boolean isDefault;

        /**
         * 展示的文案Id
         */
        private String msgId;

        /**
         * 展示的文案信息
         */
        private String msgText;

        /**
         * 扩展文案Id
         */
        private String extMsgId;

        /**
         * 扩展文案信息
         */
        private String extMsgText;


        private Integer instChangeType;

        /**
         * 文案对应的规则
         */
        private EcsTransitionConfigRule rule;

        public String getMsgText() {
            return msgText;
        }

        public void setMsgText(String msgText) {
            this.msgText = msgText;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public EcsTransitionConfigRule getRule() {
            return rule;
        }

        public void setRule(EcsTransitionConfigRule rule) {
            this.rule = rule;
        }

        public Integer getInstChangeType() {
            return instChangeType;
        }

        public void setInstChangeType(Integer instChangeType) {
            this.instChangeType = instChangeType;
        }

        public Boolean getIsDefault() {
            return this.isDefault;
        }

        public void setIsDefault(Boolean isDefault) {
            this.isDefault = isDefault;
        }

        public String getExtMsgId() {
            return extMsgId;
        }

        public void setExtMsgId(String extMsgId) {
            this.extMsgId = extMsgId;
        }

        public String getExtMsgText() {
            return extMsgText;
        }

        public void setExtMsgText(String extMsgText) {
            this.extMsgText = extMsgText;
        }
    }

    public static class EcsTransitionConfigRule {


        /** 0 过保迁移, 1 非过保迁移 **/
        private List<Integer> transType;

        /** 比如：cn-qingdao-cm5-a01 **/
        private List<String> regionNo;

        private List<Long> aliUid;

        private List<String> bid;

        /** ecs,disk **/
        private List<String> resourceType;

        private List<Long> zoneId;

        private List<Long> targetZoneId;

        private List<Integer> networkTransType;

        /** instance type 实例规格 **/
        private List<String> instanceType;

        /** 磁盘类型 ioX **/
        private List<String> ioX;

        private List<String> instanceId;


        public List<Integer> getTransType() {
            return transType;
        }

        public void setTransType(List<Integer> transType) {
            this.transType = transType;
        }

        public List<String> getRegionNo() {
            return regionNo;
        }

        public void setRegionNo(List<String> regionNo) {
            this.regionNo = regionNo;
        }

        public List<Long> getAliUid() {
            return aliUid;
        }

        public void setAliUid(List<Long> aliUid) {
            this.aliUid = aliUid;
        }

        public List<String> getBid() {
            return bid;
        }

        public void setBid(List<String> bid) {
            this.bid = bid;
        }

        public List<String> getResourceType() {
            return resourceType;
        }

        public void setResourceType(List<String> resourceType) {
            this.resourceType = resourceType;
        }

        public List<Long> getZoneId() {
            return zoneId;
        }

        public void setZoneId(List<Long> zoneId) {
            this.zoneId = zoneId;
        }

        public List<Long> getTargetZoneId() {
            return targetZoneId;
        }

        public void setTargetZoneId(List<Long> targetZoneId) {
            this.targetZoneId = targetZoneId;
        }

        public List<Integer> getNetworkTransType() {
            return networkTransType;
        }

        public void setNetworkTransType(List<Integer> networkTransType) {
            this.networkTransType = networkTransType;
        }

        public List<String> getInstanceType() {
            return instanceType;
        }

        public void setInstanceType(List<String> instanceType) {
            this.instanceType = instanceType;
        }

        public List<String> getIoX() {
            return ioX;
        }

        public void setIoX(List<String> ioX) {
            this.ioX = ioX;
        }

        public List<String> getInstanceId() {
            return instanceId;
        }

        public void setInstanceId(List<String> instanceId) {
            this.instanceId = instanceId;
        }

    }
}
