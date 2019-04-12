package com.alibaba.json.test.tmall;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * ���������������
 * User: jingxian.lzg
 * Date: 2015/8/17
 * Time: 16:26
 */
public class RateSearchItemDO {
    /**
     * ����id
     */
    @JSONField(name = "feed_id")
    private long feedId;
    /**
     * ��Ʒid
     */
    @JSONField(name = "item_id")
    private long aucNumId;
    /**
     * ���в���ֵ
     */
    @JSONField(name = "rate")
    private int rate;
    /**
     * �����Ƿ���ͼƬ
     */
    @JSONField(name = "rater_pic")
    private int raterPic;
    /**
     * ������(����id)
     */
    @JSONField(name = "rated_uid")
    private long ratedUid;
    /**
     * ������(���id)
     */
    @JSONField(name = "rater_uid")
    private long raterUid;
    /**
     * ���������ǳ�
     */
    @JSONField(name = "rated_user_nick")
    private String ratedUserNick;
    /**
     * �������ǳ�
     */
    @JSONField(name = "rater_user_nick")
    private String raterUserNick;
    /**
     * ����״̬
     */
    @JSONField(name = "status")
    private int status;
    /**
     * ��������
     */
    @JSONField(name = "feedback")
    private String feedback;
    /**
     * �����Ƿ�������
     */
    @JSONField(name = "validfeedback")
    private int validfeedback;
    /**
     * �ظ�
     */
    @JSONField(name = "reply")
    private String reply;
    /**
     * ҵ������
     */
    @JSONField(name = "biz_type")
    private int bizType;
    /**
     * �㷨���ֵ
     */
    @JSONField(name = "sort_weight")
    private int sortWeight;
    /**
     * ����ʱ���
     */
    @JSONField(name = "gmt_create")
    private long gmtCreateStamp;
    /**
     * ���ü���
     */
    @JSONField(name = "vote_useful")
    private int voteUseful;
    /**
     * �����ֶ�
     */
    @JSONField(name = "attributes")
    private String attributes;
    /**
     * ���Ի��Ĵ�ֱ����
     */
    @JSONField(name = "details")
    private String properties;
    /**
     * �Ƿ�����
     */
    @JSONField(name = "anony")
    private int anony;
    /**
     * ��Ǽ�¼��Դ
     */
    @JSONField(name = "source")
    private long source;
    /**
     * �������ʱ��
     */
    @JSONField(name = "gmt_trade_finished")
    private long gmtTradeFinishedStamp;
    /**
     * �����߽��׽�ɫ
     */
    @JSONField(name = "rater_type")
    private int raterType;
    /**
     * ���ۼƷ�״̬
     */
    @JSONField(name = "validscore")
    private int validscore;
    /**
     * ��Ʒһ����Ŀid
     */
    @JSONField(name = "cate_level1_id")
    private long rootCatId;
    /**
     * ��ƷҶ����Ŀ
     */
    @JSONField(name = "cate_id")
    private long leafCatId;
    /**
     * ������Id
     * */
    @JSONField(name = "mord_id")
    private long parentTradeId;
    /**
     * �Ӷ���Id
     * */
    @JSONField(name = "order_id")
    private long tradeId;
    /**
     * �Ƿ����׷��
     */
    @JSONField(name = "append")
    private int append;
    /**
     * ׷��״̬
     */
    @JSONField(name = "append_status")
    private int appendStatus;
    /**
     * ׷������
     */
    @JSONField(name = "append_feedback")
    private String appendFeedback;
    /**
     * ׷���ظ�����
     */
    @JSONField(name = "append_reply")
    private String appendReply;
    /**
     * ׷��ʱ���
     */
    @JSONField(name = "append_create")
    private long appendCreateStamp;
    /**
     * ׷���Ƿ����ͼƬ
     */
    @JSONField(name = "append_pic")
    private int appendPic;
    /**
     * ׷����¼id
     */
    @JSONField(name = "append_feed_id")
    private long appendFeedId;
    /**
     * ׷�������ֶ�
     */
    @JSONField(name = "append_attributes")
    private String appendAttributes;
    /**
     * ׷����ֱ�����ֶ�
     */
    @JSONField(name = "append_properties")
    private String appendProperties;
    /**
     * �㷨�����ֵ
     */
    @JSONField(name = "algo_sort")
    private long algoSort;
    /**
     * �۵���־
     */
    @JSONField(name = "fold_flag")
    private int foldFlag;
    /**
     * �㷨�����ֶ�
     */
    @JSONField(name = "reason")
    private String reason;
    /**
     * �㷨�����ֶ�
     */
    @JSONField(name = "other")
    private String other;
    /**
     * ���ӡ���ǩ
     */
    @JSONField(name = "expression_auc")
    private String expressionAuc;
    /**
     * ���ӡ���ǩ����λ��
     */
    @JSONField(name = "position")
    private String position;
    /**
     * ���Ի��û���ǩ
     * */
    @JSONField(name = "personalized_tag")
    private String personalizedTag;
    /**
     * ����ͼƬ
     */
    @JSONField(name = "main_pic_json")
    private String mainPicJson;
    /**
     * ׷��ͼƬ
     */
    @JSONField(name = "append_pic_json")
    private String appendPicJson;
    /**
     * ���������Ϣ
     */
    @JSONField(name = "main_component_json")
    private String mainComponentJson;
    /**
     * ׷�������Ϣ
     */
    @JSONField(name = "append_component_json")
    private String appendComponentJson;
    /**
     * �Ƿ�Ϊϵͳ����
     */
    @JSONField(name = "is_auto")
    private String auto;

    public long getFeedId () {
        return feedId;
    }

    public void setFeedId (long feedId) {
        this.feedId = feedId;
    }

    public long getRatedUid () {
        return ratedUid;
    }

    public void setRatedUid (long ratedUid) {
        this.ratedUid = ratedUid;
    }

    public long getRaterUid () {
        return raterUid;
    }

    public void setRaterUid (long raterUid) {
        this.raterUid = raterUid;
    }

    public String getRaterUserNick () {
        return raterUserNick;
    }

    public void setRaterUserNick (String raterUserNick) {
        this.raterUserNick = raterUserNick;
    }

    public String getFeedback () {
        return feedback;
    }

    public void setFeedback (String feedback) {
        this.feedback = feedback;
    }

    public int getRate () {
        return rate;
    }

    public void setRate (int rate) {
        this.rate = rate;
    }

    public int getStatus () {
        return status;
    }

    public void setStatus (int status) {
        this.status = status;
    }

    public String getReply () {
        return reply;
    }

    public void setReply (String reply) {
        this.reply = reply;
    }

    public int getBizType () {
        return bizType;
    }

    public void setBizType (int bizType) {
        this.bizType = bizType;
    }

    public int getSortWeight () {
        return sortWeight;
    }

    public void setSortWeight (int sortWeight) {
        this.sortWeight = sortWeight;
    }

    public int getVoteUseful () {
        return voteUseful;
    }

    public void setVoteUseful (int voteUseful) {
        this.voteUseful = voteUseful;
    }

    public String getAttributes () {
        return attributes;
    }

    public void setAttributes (String attributes) {
        this.attributes = attributes;
    }

    public String getProperties () {
        return properties;
    }

    public void setProperties (String properties) {
        this.properties = properties;
    }

    public int getAppendStatus () {
        return appendStatus;
    }

    public void setAppendStatus (int appendStatus) {
        this.appendStatus = appendStatus;
    }

    public String getAppendFeedback () {
        return appendFeedback;
    }

    public void setAppendFeedback (String appendFeedback) {
        this.appendFeedback = appendFeedback;
    }

    public String getAppendReply () {
        return appendReply;
    }

    public void setAppendReply (String appendReply) {
        this.appendReply = appendReply;
    }

    public long getAppendFeedId () {
        return appendFeedId;
    }

    public void setAppendFeedId (long appendFeedId) {
        this.appendFeedId = appendFeedId;
    }

    public String getAppendAttributes () {
        return appendAttributes;
    }

    public void setAppendAttributes (String appendAttributes) {
        this.appendAttributes = appendAttributes;
    }

    public String getAppendProperties () {
        return appendProperties;
    }

    public void setAppendProperties (String appendProperties) {
        this.appendProperties = appendProperties;
    }

    public int getRaterPic () {
        return raterPic;
    }

    public void setRaterPic (int raterPic) {
        this.raterPic = raterPic;
    }

    public int getValidfeedback () {
        return validfeedback;
    }

    public void setValidfeedback (int validfeedback) {
        this.validfeedback = validfeedback;
    }

    public int getAppendPic () {
        return appendPic;
    }

    public void setAppendPic (int appendPic) {
        this.appendPic = appendPic;
    }

    public long getGmtCreateStamp () {
        return gmtCreateStamp;
    }

    public void setGmtCreateStamp (long gmtCreateStamp) {
        this.gmtCreateStamp = gmtCreateStamp;
    }

    public long getAppendCreateStamp () {
        return appendCreateStamp;
    }

    public void setAppendCreateStamp (long appendCreateStamp) {
        this.appendCreateStamp = appendCreateStamp;
    }

    public int getAppend () {
        return append;
    }

    public void setAppend (int append) {
        this.append = append;
    }

    public boolean haveAppend () {
        return append == 1;
    }

    public String getRatedUserNick () {
        return ratedUserNick;
    }

    public void setRatedUserNick (String ratedUserNick) {
        this.ratedUserNick = ratedUserNick;
    }

    public int getAnony () {
        return anony;
    }

    public void setAnony (int anony) {
        this.anony = anony;
    }

    public long getGmtTradeFinishedStamp () {
        return gmtTradeFinishedStamp;
    }

    public void setGmtTradeFinishedStamp (long gmtTradeFinishedStamp) {
        this.gmtTradeFinishedStamp = gmtTradeFinishedStamp;
    }

    public int getRaterType () {
        return raterType;
    }

    public void setRaterType (int raterType) {
        this.raterType = raterType;
    }

    public int getValidscore () {
        return validscore;
    }

    public void setValidscore (int validscore) {
        this.validscore = validscore;
    }

    public long getAlgoSort () {
        return algoSort;
    }

    public void setAlgoSort (long algoSort) {
        this.algoSort = algoSort;
    }

    public int getFoldFlag () {
        return foldFlag;
    }

    public void setFoldFlag (int foldFlag) {
        this.foldFlag = foldFlag;
    }

    public String getReason () {
        return reason;
    }

    public void setReason (String reason) {
        this.reason = reason;
    }

    public String getOther () {
        return other;
    }

    public void setOther (String other) {
        this.other = other;
    }

    public String getExpressionAuc () {
        return expressionAuc;
    }

    public void setExpressionAuc (String expressionAuc) {
        this.expressionAuc = expressionAuc;
    }

    public String getPosition () {
        return position;
    }

    public void setPosition (String position) {
        this.position = position;
    }

    public long getAucNumId () {
        return aucNumId;
    }

    public void setAucNumId (long aucNumId) {
        this.aucNumId = aucNumId;
    }

    public long getSource () {
        return source;
    }

    public void setSource (long source) {
        this.source = source;
    }

    public long getRootCatId () {
        return rootCatId;
    }

    public void setRootCatId (long rootCatId) {
        this.rootCatId = rootCatId;
    }

    public long getLeafCatId() {
        return leafCatId;
    }

    public void setLeafCatId(long leafCatId) {
        this.leafCatId = leafCatId;
    }

    public String getMainPicJson() {
        return mainPicJson;
    }

    public void setMainPicJson(String mainPicJson) {
        this.mainPicJson = mainPicJson;
    }

    public String getAppendPicJson() {
        return appendPicJson;
    }

    public void setAppendPicJson(String appendPicJson) {
        this.appendPicJson = appendPicJson;
    }

    public String getMainComponentJson() {
        return mainComponentJson;
    }

    public void setMainComponentJson(String mainComponentJson) {
        this.mainComponentJson = mainComponentJson;
    }

    public String getAppendComponentJson() {
        return appendComponentJson;
    }

    public void setAppendComponentJson(String appendComponentJson) {
        this.appendComponentJson = appendComponentJson;
    }

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }

    public long getParentTradeId() {
        return parentTradeId;
    }

    public void setParentTradeId(long parentTradeId) {
        this.parentTradeId = parentTradeId;
    }

    public long getTradeId() {
        return tradeId;
    }

    public void setTradeId(long tradeId) {
        this.tradeId = tradeId;
    }

    public String getPersonalizedTag() {
        return personalizedTag;
    }

    public void setPersonalizedTag(String personalizedTag) {
        this.personalizedTag = personalizedTag;
    }

}
