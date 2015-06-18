/**
 * Project: dubbo.test
 * 
 * File Created at 2010-11-30
 * $Id: Image.java 75806 2011-02-18 09:08:30Z tony.chenl $
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

import java.io.InputStream;
import java.io.Serializable;

/**
 * TODO Comment of Image
 * 
 * @author tony.chenl
 */
public class Image implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 616779453943392868L;
    String                    name;
    InputStream               is;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InputStream getIs() {
        return is;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }

}
