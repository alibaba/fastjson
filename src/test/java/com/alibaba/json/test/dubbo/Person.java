/**
 * Project: dubbo.test
 * 
 * File Created at 2010-11-17
 * $Id: Person.java 77622 2011-03-03 08:31:45Z ding.lid $
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

/**
 * TODO Comment of Person
 * 
 * @author tony.chenl
 */
public class Person implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    String                    personId;

    String                    loginName;

    PersonStatus              status;

    String                    email;

    String                    penName;

    PersonInfo                infoProfile;

    public Person() {

    }

    public Person(String id) {
        this.personId = id;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public PersonInfo getInfoProfile() {
        return infoProfile;
    }

    public void setInfoProfile(PersonInfo infoProfile) {
        this.infoProfile = infoProfile;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setStatus(PersonStatus status) {
        this.status = status;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPenName(String penName) {
        this.penName = penName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getLoginName() {
        return this.loginName;
    }

    public PersonStatus getStatus() {
        return this.status;
    }

    public String getPenName() {
        return penName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((infoProfile == null) ? 0 : infoProfile.hashCode());
        result = prime * result + ((loginName == null) ? 0 : loginName.hashCode());
        result = prime * result + ((penName == null) ? 0 : penName.hashCode());
        result = prime * result + ((personId == null) ? 0 : personId.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
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
        Person other = (Person) obj;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (infoProfile == null) {
            if (other.infoProfile != null)
                return false;
        } else if (!infoProfile.equals(other.infoProfile))
            return false;
        if (loginName == null) {
            if (other.loginName != null)
                return false;
        } else if (!loginName.equals(other.loginName))
            return false;
        if (penName == null) {
            if (other.penName != null)
                return false;
        } else if (!penName.equals(other.penName))
            return false;
        if (personId == null) {
            if (other.personId != null)
                return false;
        } else if (!personId.equals(other.personId))
            return false;
        if (status != other.status)
            return false;
        return true;
    }

}
