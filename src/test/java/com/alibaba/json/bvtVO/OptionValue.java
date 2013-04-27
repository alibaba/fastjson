/**
 * Project: rowan.share-1.0.0
 * 
 * File Created at 2011-11-24
 * $Id$
 * 
 * Copyright 1999-2100 Alibaba.com Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Alibaba Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Alibaba.com.
 */
package com.alibaba.json.bvtVO;

import java.io.Serializable;

/**
 * 类OptionValue.java的实现描述：TODO 类实现描述
 * 
 * @author lei.yaol 2011-12-27 下午03:41:43
 */
public class OptionValue<E extends Serializable> implements Serializable {

    private static final long serialVersionUID = -1158546247925194748L;

    private E                 value;

    /**
     * set option value
     * 
     * @param value
     */
    public void setValue(E value) {
        this.value = value;
    }

    /**
     * get option value
     * 
     * @return E
     */
    public E getValue() {
        return value;
    }
}
