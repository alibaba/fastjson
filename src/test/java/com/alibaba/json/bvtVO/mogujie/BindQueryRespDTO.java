package com.alibaba.json.bvtVO.mogujie;

import java.io.Serializable;

/**
 * Created by wenshao on 16/03/2017.
 */
public class BindQueryRespDTO {

    /**
     * version
     */
    public int            f0;

    /**
     * is online test
     */
    public int           f1;

    /**
     * http referer
     */
    public String referer;

    /**
     * 签约ID
     */
    public Long bindId;

    /**
     * 签约银行卡ID
     */
    public int    bankCardId;

    /**
     * 签约号
     */
    public String bindNo;

    /**
     * 用户ID
     */
    public Long  userId;

    /**
     * 签约状态
     */
    public Integer  status;

    /**
     * 签约时间（10位时间戳）
     */
    public Long bindTime;

    /**
     * 签约银行卡号
     */
    public String   cardNo;

    /**
     * 签约银行卡号标记
     */
    public String cardNoMark;

    /**
     * 签约银行卡号缩写
     */
    public String cardNoClip;

    /**
     * 银行ID
     */
    public String bankId;

    /**
     * 银行名称
     */
    public String bankName;

    /**
     * 银行LOGO
     */
    public String bankLogo;

    /**
     * 银行背景色
     */
    public String bankColor;

    /**
     * 银行卡类
     */
    public Integer cardType;

    /**
     * 银行卡类描述
     */
    public String cardTypeDesc;

    /**
     * 持卡人姓名缩写
     */
    public String cardHolderNameClip;

    /**
     * 签约银行卡预留手机号
     */
    public String mobile;

    /**
     * 签约银行卡预留手机号缩写
     */

    public String mobileClip;

    /**
     * 银行卡开户省 see new
     */

    public String province;

    /**
     * 银行卡开户市
     */
    public String city;

    /**
     * 平安提现通道-大小额通道编号
     */
    public String pinganBankCode;

    /**
     * 平安提现通道-超网通道编号
     */
    public String pinganSuperBankCode;

    /**
     * 开户支行
     */
    public String subBank;

    /**
     * 联行号
     */
    public String cnapsCode;

    /**
     * 渠道签约ID
     */
    public Long     channelBindId;

    /**
     * 渠道签约号
     */
    public String   channelBindNo;

    /**
     * 渠道签约类型
     */
    public Integer  channelBindType;

    public String  subBankDesc;


    /**
     * 银行卡信息
     */
    public BankCard bankCard;


}