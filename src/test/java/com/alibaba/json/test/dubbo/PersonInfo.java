/**
 * Project: dubbo.test
 * 
 * File Created at 2010-11-17
 * $Id: PersonInfo.java 77622 2011-03-03 08:31:45Z ding.lid $
 * 
 * Copyright 2008 Alibaba.com Croporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Alibaba Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Alibaba.com.
 */
package com.alibaba.json.test.dubbo;

import java.io.Serializable;
import java.util.List;

/**
 * TODO Comment of PersonInfoModel
 * 
 * @author tony.chenl
 */
public class PersonInfo implements Serializable {
    private static final long serialVersionUID = 7443011149612231882L;

    List<Phone>               phones;

    Phone                     fax;

    FullAddress               fullAddress;

    String                    mobileNo;

    String                    name;

    boolean                   male;

    boolean                   female;

    String                    department;

    String                    jobTitle;

    String                    homepageUrl;

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public boolean isFemale() {
        return female;
    }

    public void setFemale(boolean female) {
        this.female = female;
    }

    public void setFax(Phone fax) {
        this.fax = fax;
    }

    public void setFullAddress(FullAddress fullAddress) {
        this.fullAddress = fullAddress;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setHomepageUrl(String homepageUrl) {
        this.homepageUrl = homepageUrl;
    }

    public String getDepartment() {
        return department;
    }

    public Phone getFax() {
        return fax;
    }

    public FullAddress getFullAddress() {
        return fullAddress;
    }

    public String getHomepageUrl() {
        return homepageUrl;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((department == null) ? 0 : department.hashCode());
        result = prime * result + ((fax == null) ? 0 : fax.hashCode());
        result = prime * result + (female ? 1231 : 1237);
        result = prime * result + ((fullAddress == null) ? 0 : fullAddress.hashCode());
        result = prime * result + ((homepageUrl == null) ? 0 : homepageUrl.hashCode());
        result = prime * result + ((jobTitle == null) ? 0 : jobTitle.hashCode());
        result = prime * result + (male ? 1231 : 1237);
        result = prime * result + ((mobileNo == null) ? 0 : mobileNo.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((phones == null) ? 0 : phones.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PersonInfo other = (PersonInfo) obj;
        if (department == null) {
            if (other.department != null)
                return false;
        } else if (!department.equals(other.department))
            return false;
        if (fax == null) {
            if (other.fax != null)
                return false;
        } else if (!fax.equals(other.fax))
            return false;
        if (female != other.female)
            return false;
        if (fullAddress == null) {
            if (other.fullAddress != null)
                return false;
        } else if (!fullAddress.equals(other.fullAddress))
            return false;
        if (homepageUrl == null) {
            if (other.homepageUrl != null)
                return false;
        } else if (!homepageUrl.equals(other.homepageUrl))
            return false;
        if (jobTitle == null) {
            if (other.jobTitle != null)
                return false;
        } else if (!jobTitle.equals(other.jobTitle))
            return false;
        if (male != other.male)
            return false;
        if (mobileNo == null) {
            if (other.mobileNo != null)
                return false;
        } else if (!mobileNo.equals(other.mobileNo))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (phones == null) {
            if (other.phones != null)
                return false;
        } else if (!phones.equals(other.phones))
            return false;
        return true;
    }

}
