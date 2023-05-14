package com.alibaba.json.bvtVO.alipay;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

public class PlatformDepartmentVO {
    @JSONField(ordinal=1)
    private String id ;
    @JSONField(ordinal=2)
    private String label ;
    @JSONField(ordinal=3)
    private String value;
    @JSONField(ordinal=4)
    private String  parentId;
    @JSONField(ordinal=5)
    private String  parentLabel;
    @JSONField(ordinal=6)
    private String companyId;
    @JSONField(ordinal=7)
    private String departCode;
    @JSONField(ordinal=8)
    private String memo;
    @JSONField(ordinal=9)
    private String departOrgCode;
    @JSONField(ordinal=10)
    private String contact;
    @JSONField(ordinal=11)
    private String mobile;
    @JSONField(ordinal=12)
    private String departType;
    @JSONField(serialize=false)
    private String ipId;
    @JSONField(serialize=false)
    private String ipRoleId;
    @JSONField(serialize=false)
    private PlatformDepartmentVO parent;
    @JSONField(ordinal=6,name="ChildNodes")
    private List<PlatformDepartmentVO> childNodes =new ArrayList<PlatformDepartmentVO>();
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public String getCompanyId() {
        return companyId;
    }
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDepartCode() {
        return departCode;
    }
    public void setDepartCode(String departCode) {
        this.departCode = departCode;
    }
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public PlatformDepartmentVO getParent() {
        return parent;
    }
    public void setParent(PlatformDepartmentVO parent) {
        this.parent = parent;
    }
    public List<PlatformDepartmentVO> getChildNodes() {
        return childNodes;
    }
    public void setChildNodes(List<PlatformDepartmentVO> childNodes) {
        this.childNodes = childNodes;
    }

    /**
     * Getter method for property <tt>departType</tt>.
     *
     * @return property value of departType
     */
    public String getDepartType() {
        return departType;
    }

    /**
     * Setter method for property <tt>departType</tt>.
     *
     * @param departType  value to be assigned to property departType
     */
    public void setDepartType(String departType) {
        this.departType = departType;
    }

    /**
     * Getter method for property <tt>parentLabel</tt>.
     *
     * @return property value of parentLabel
     */
    public String getParentLabel() {
        return parentLabel;
    }

    /**
     * Setter method for property <tt>parentLabel</tt>.
     *
     * @param parentLabel  value to be assigned to property parentLabel
     */
    public void setParentLabel(String parentLabel) {
        this.parentLabel = parentLabel;
    }

    /**
     * Getter method for property <tt>departOrgCode</tt>.
     *
     * @return property value of departOrgCode
     */
    public String getDepartOrgCode() {
        return departOrgCode;
    }

    /**
     * Setter method for property <tt>departOrgCode</tt>.
     *
     * @param departOrgCode  value to be assigned to property departOrgCode
     */
    public void setDepartOrgCode(String departOrgCode) {
        this.departOrgCode = departOrgCode;
    }

    /**
     * Getter method for property <tt>contact</tt>.
     *
     * @return property value of contact
     */
    public String getContact() {
        return contact;
    }

    /**
     * Setter method for property <tt>contact</tt>.
     *
     * @param contact  value to be assigned to property contact
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * Getter method for property <tt>mobile</tt>.
     *
     * @return property value of mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Setter method for property <tt>mobile</tt>.
     *
     * @param mobile  value to be assigned to property mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * Getter method for property <tt>ipRoleId</tt>.
     *
     * @return property value of ipRoleId
     */
    public String getIpRoleId() {
        return ipRoleId;
    }

    /**
     * Setter method for property <tt>ipRoleId</tt>.
     *
     * @param ipRoleId  value to be assigned to property ipRoleId
     */
    public void setIpRoleId(String ipRoleId) {
        this.ipRoleId = ipRoleId;
    }

    /**
     * Getter method for property <tt>ipId</tt>.
     *
     * @return property value of ipId
     */
    public String getIpId() {
        return ipId;
    }

    /**
     * Setter method for property <tt>ipId</tt>.
     *
     * @param ipId  value to be assigned to property ipId
     */
    public void setIpId(String ipId) {
        this.ipId = ipId;
    }

    public PlatformDepartmentVO() {

    }
//    public PlatformDepartmentVO(String id, String label, String value, String parentId,
//                                String companyId) {
//        this.id = id;
//        this.label = label;
//        this.value = value;
//        this.parentId = parentId;
//        this.companyId = companyId;
//    }


    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if(null==this.getId()){
            return false;
        }
        final PlatformDepartmentVO other = (PlatformDepartmentVO) obj;
        if(!this.getId().equals(other.getId())) {
            return false;
        }
        return true;
    }

}