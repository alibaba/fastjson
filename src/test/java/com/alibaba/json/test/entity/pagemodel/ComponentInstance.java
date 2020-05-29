package com.alibaba.json.test.entity.pagemodel;

/**
 * ��ComponentInstance.java��ʵ�������� ���ʵ��ģ��.
 * 
 * @author jiajie.yujj @ 2010-11-29 ����09:53:39
 * @author naipei.chennp 2010-12-7 ����09:47:50
 */
public abstract class ComponentInstance {

    protected Long   sid;
    protected String cid;

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

}
