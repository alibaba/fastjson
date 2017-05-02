package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;

/**
 * Created by hzfengboyang on 2016/5/4.
 */
public class GoodsDelivery implements Serializable {

    private static final long serialVersionUID = -6435669257750999530L;
    private  String address;           // 配送地址
    private  String districtCode;      // 区域id
    private  String postage;           // 邮费描述 html
    private  String desc;              // 配送描述，次日达
    private  String contactId;         // 用户地址id

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPostage() {
        return postage;
    }

    public void setPostage(String postage) {
        this.postage = postage;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }


}
