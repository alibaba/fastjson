/**
 * Project: morgan.domain
 * 
 * File Created at 2009-6-11
 * $Id: Phone.java 77622 2011-03-03 08:31:45Z ding.lid $
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
 * 电话号码
 * 
 * @author xk1430
 */
public class Phone implements Serializable {

    private static final long serialVersionUID = 4399060521859707703L;

    private String            country;

    private String            area;

    private String            number;

    private String            extensionNumber;

    public Phone() {
    }

    public Phone(String country, String area, String number, String extensionNumber) {
        this.country = country;
        this.area = area;
        this.number = number;
        this.extensionNumber = extensionNumber;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setExtensionNumber(String extensionNumber) {
        this.extensionNumber = extensionNumber;
    }

    public String getCountry() {
        return country;
    }

    public String getArea() {
        return area;
    }

    public String getNumber() {
        return number;
    }

    public String getExtensionNumber() {
        return extensionNumber;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((area == null) ? 0 : area.hashCode());
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        result = prime * result + ((extensionNumber == null) ? 0 : extensionNumber.hashCode());
        result = prime * result + ((number == null) ? 0 : number.hashCode());
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
        Phone other = (Phone) obj;
        if (area == null) {
            if (other.area != null)
                return false;
        } else if (!area.equals(other.area))
            return false;
        if (country == null) {
            if (other.country != null)
                return false;
        } else if (!country.equals(other.country))
            return false;
        if (extensionNumber == null) {
            if (other.extensionNumber != null)
                return false;
        } else if (!extensionNumber.equals(other.extensionNumber))
            return false;
        if (number == null) {
            if (other.number != null)
                return false;
        } else if (!number.equals(other.number))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (country != null && country.length() > 0) {
            sb.append(country);
            sb.append("-");
        }
        if (area != null && area.length() > 0) {
            sb.append(area);
            sb.append("-");
        }
        if (number != null && number.length() > 0) {
            sb.append(number);
        }
        if (extensionNumber != null && extensionNumber.length() > 0) {
            sb.append("-");
            sb.append(extensionNumber);
        }
        return sb.toString();
    }

}
