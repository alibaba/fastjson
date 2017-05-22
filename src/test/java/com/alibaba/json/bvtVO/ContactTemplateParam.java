package com.alibaba.json.bvtVO;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.alibaba.fastjson.annotation.JSONField;


public class ContactTemplateParam implements Serializable {

    private static final long serialVersionUID = 1L;



    public ContactTemplateParam() {
        // TODO Auto-generated constructor stub
    }

    /** 审核状态 **/
    private AuditStatusType auditStatus;



    public AuditStatusType getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(AuditStatusType auditStatus) {
        this.auditStatus = auditStatus;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
