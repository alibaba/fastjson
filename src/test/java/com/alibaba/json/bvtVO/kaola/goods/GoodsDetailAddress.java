package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;

/**
 * Created by hzfengboyang on 2016/5/4.
 */
public class GoodsDetailAddress implements Serializable {

    private static final long serialVersionUID = 6879594367057909143L;
    private long     id;
    private String   userName;
    private String   mobile;
    private String   address;
    private String   districtCode;
    private String   districtName;
    private String   cityName;
    private String   provinceName;

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }




}
