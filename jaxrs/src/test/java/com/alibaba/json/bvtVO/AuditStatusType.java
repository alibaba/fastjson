package com.alibaba.json.bvtVO;

/**
 * Created by wenshao on 10/02/2017.
 */
public enum AuditStatusType implements IntEnum<AuditStatusType> {
    AUDIT_FAILURE(0, "审核失败", "FAILED"),
    AUDIT_SUCCESS(1, "成功", "SUCCEED"),
    AUDIT_NO_SUBMIT(2, "未实名认证", "NONAUDIT"),
    AUDIT_SUBMIT(3, "审核中", "AUDITING");

    private int code;
    private String desc;
    private String enCode;

    private AuditStatusType(int code) {
        this.code = code;
    }

    private AuditStatusType(int code, String desc, String enCode) {
        this.code = code;
        this.desc = desc;
        this.enCode = enCode;
    }

    public static AuditStatusType valuesOf(String enCode) {
        AuditStatusType[] arr$ = values();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            AuditStatusType temp = arr$[i$];
            if(temp.getEnCode().equals(enCode)) {
                return temp;
            }
        }

        return null;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getEnCode() {
        return this.enCode;
    }

    public int getCode() {
        return this.code;
    }
}
