package com.alibaba.json.bvtVO;

import java.io.Serializable;

/**
 * ��WarehouseInfo.java��ʵ���������ֿ���Ϣ����
 * 
 * @author maik.wangz 2011-12-12 ����09:50:32
 */
public class WareHouseInfo implements Serializable {

    private static final long serialVersionUID = 6102232214244738211L;
    // ʡ��ID
    private String            provinceId;
    // ʡ�����
    private String            provinceName;
    // ����ID
    private String            cityId;
    // �������
    private String            cityName;
    // ����ID
    private String            areaId;
    // ��������
    private String            areaName;
    // �ֿ����
    private String            houseArea;
    // �ֿ�ͼƬ
    private Image[]           images;

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getHouseArea() {
        return houseArea;
    }

    public void setHouseArea(String houseArea) {
        this.houseArea = houseArea;
    }

    public Image[] getImages() {
        return images;
    }

    public void setImages(Image[] images) {
        this.images = images;
    }

}
