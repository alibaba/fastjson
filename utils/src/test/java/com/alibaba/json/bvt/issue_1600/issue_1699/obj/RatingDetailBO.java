package com.alibaba.json.bvt.issue_1600.issue_1699.obj;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.json.bvt.issue_1600.issue_1699.def.InnerTypeMEnum;
import com.alibaba.json.bvt.issue_1600.issue_1699.def.RatingDetailIsJoinMEnum;
import com.alibaba.json.bvt.issue_1600.issue_1699.def.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.Map;

/**
 *
 */
public class RatingDetailBO implements Serializable {

    private static final long serialVersionUID = 6413142622719509002L;

    /**
     * *
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户NICK
     */
    private String userNick;

    /**
     * 消息ID
     */
    private Long mesId;

    /**
     * 事件类型， 枚举值参照：ra_event_object.type
     */
    private String eventType;

    /**
     * 唯一去重号
     */
    private String bizId;

    /**
     * 序列号
     */
    private Integer indexNum;

    /**
     * 业务类型（同原始消息)
     */
    private String bizType;

    /**
     * 业务交易号
     */
    private String outBizId;

    /**
     * 主订单ID
     */
    private Long pTradeId;

    /**
     * 子订单ID
     */
    private Long tradeId;

    /**
     * 业务交易时间
     */
    private Date bizTime;

    /**
     * 消息接收时间
     */
    private Date mesReceiveTime;

    /**
     * 处理时间
     */
    private Date dealTime;

    /**
     * 详单科目编号
     */
    private Long itemId;

    /**
     * 详单类型： 1、普通详单 2、分成详单 3、预收分成详单
     */
    private RatingDetailTypeMEnum detailType;

    /**
     * 原始金额
     */
    private BigDecimal quantity;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 费率编号
     */
    private Long rateDefineId;

    /**
     * 计费因子
     */
    private BigDecimal proration;

    /**
     * 产品编号
     */
    private Long prodId;

    /**
     * 扩展信息
     */
    private Map<String, String> extendInfo;

    private Map<String, String> rateParams;

    private Currency currency;

    private InnerTypeMEnum innerTable;
    private Long innerId;

    public Map<String, String> getRateParams() {
        return rateParams;
    }

    public void setRateParams(Map<String, String> rateParams) {
        this.rateParams = rateParams;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public InnerTypeMEnum getInnerTable() {
        return innerTable;
    }

    public void setInnerTable(InnerTypeMEnum innerTable) {
        this.innerTable = innerTable;
    }

    public Long getInnerId() {
        return innerId;
    }

    public void setInnerId(Long innerId) {
        this.innerId = innerId;
    }

    public void setExtendInfo(Map<String, String> extendInfo) {
        this.extendInfo = extendInfo;
    }

    /**
     * 环境标识
     */
    private String ownSign;

    /**
     * 帐单ID， 记账结束后回写
     */
    private Long billId;

    /**
     * 版本编号
     */
    private Integer version;

    /**
     * 是否合并付款： 0、否 1、是
     */
    private RatingDetailIsJoinMEnum isJoin;

    /**
     * 优先级， 值越大，优先级越高
     */
    private Integer priority;

    /**
     * 状态： 0、初始 1、处理成功； 2、处理失败； 3、等待合并；
     */
    private RatingDetailStatusMEnum status;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 交易项目：0、交易；1、退款
     */
    private FeeTypeMEnum feeType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public Long getMesId() {
        return mesId;
    }

    public void setMesId(Long mesId) {
        this.mesId = mesId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getOutBizId() {
        return outBizId;
    }

    public void setOutBizId(String outBizId) {
        this.outBizId = outBizId;
    }

    public Long getpTradeId() {
        return pTradeId;
    }

    public void setpTradeId(Long pTradeId) {
        this.pTradeId = pTradeId;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Date getBizTime() {
        return bizTime;
    }

    public void setBizTime(Date bizTime) {
        this.bizTime = bizTime;
    }

    public Date getMesReceiveTime() {
        return mesReceiveTime;
    }

    public void setMesReceiveTime(Date mesReceiveTime) {
        this.mesReceiveTime = mesReceiveTime;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getRateDefineId() {
        return rateDefineId;
    }

    public void setRateDefineId(Long rateDefineId) {
        this.rateDefineId = rateDefineId;
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public String getOwnSign() {
        return ownSign;
    }

    public void setOwnSign(String ownSign) {
        this.ownSign = ownSign;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public RatingDetailTypeMEnum getDetailType() {
        return detailType;
    }

    public void setDetailType(RatingDetailTypeMEnum detailType) {
        this.detailType = detailType;
    }

    public RatingDetailIsJoinMEnum getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(RatingDetailIsJoinMEnum isJoin) {
        this.isJoin = isJoin;
    }

    public RatingDetailStatusMEnum getStatus() {
        return status;
    }

    public void setStatus(RatingDetailStatusMEnum status) {
        this.status = status;
    }

    public FeeTypeMEnum getFeeType() {
        return feeType;
    }

    public void setFeeType(FeeTypeMEnum feeType) {
        this.feeType = feeType;
    }

    public Map<String, String> getExtendInfo() {
        return extendInfo;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getProration() {
        return proration;
    }

    public void setProration(BigDecimal proration) {
        this.proration = proration;
    }

    public Integer getIndexNum() {
        return indexNum;
    }

    public void setIndexNum(Integer indexNum) {
        this.indexNum = indexNum;
    }

}
