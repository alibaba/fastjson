package com.alibaba.json.bvtVO.mogujie;

import java.io.Serializable;

/**
 * Created by wenshao on 16/03/2017.
 */
public class BankCard implements Serializable {
    private static final long serialVersionUID = -8043292491053382301L;

    public static final Integer CARD_TYPE_DEBIT = 1;        //借记卡
    public static final Integer CARD_TYPE_CREDIT = 2;       //贷记卡

    private Long id;
    private String bankId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

}