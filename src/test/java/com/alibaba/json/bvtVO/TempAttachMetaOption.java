/**
 * Project: rowan.server.biz.service-1.0-SNAPSHOT
 * 
 * File Created at 2011-12-9
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
 * 类TempAttachMetaOption.java的实现描述：TODO 类实现描述
 * 
 * @author lei.yaol 2011-12-27 下午03:43:32
 */
public class TempAttachMetaOption implements Serializable {

    private static final long serialVersionUID = -8786217160252057362L;

    private Integer           id;

    private String            name;

    private String            path;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
