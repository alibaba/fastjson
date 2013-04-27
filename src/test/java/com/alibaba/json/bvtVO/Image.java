/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.alibaba.json.bvtVO;

import java.io.Serializable;

/**
 * ��Image.java��ʵ��������ͼƬ��Ϣ����
 * 
 * @author maik.wangz 2011-8-15 ����06:19:39
 */
public class Image implements Serializable {

    private static final long serialVersionUID = -6804500330834961534L;
    private String            imageUrl;

    public Image(String imageUrl){
        super();
        this.imageUrl = imageUrl;
    }

    public Image(){

    }

    public String getBigImageUrl() {
        if (imageUrl == null || imageUrl.length() == 0) {
            return "";
        }
        return ("img/" + imageUrl).replaceFirst(".jpg", ".310x310.jpg");
    }

    /** �������ͼƬ�����ϵ����·�� 150 * 150 */
    public String getSearchImageUrl() {
        if (imageUrl == null || imageUrl.length() == 0) {
            return "";
        }
        return ("img/" + imageUrl).replaceFirst(".jpg", ".search.jpg");
    }

    /** �������ͼƬ�����ϵ����·�� 100 * 100 */
    public String getSummImageUrl() {
        if (imageUrl == null || imageUrl.length() == 0) {
            return "";
        }
        return ("img/" + imageUrl).replaceFirst(".jpg", ".summ.jpg");
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
