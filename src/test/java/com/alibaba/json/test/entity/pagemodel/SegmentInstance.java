/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.alibaba.json.test.entity.pagemodel;

import java.io.Serializable;
import java.util.List;

/**
 * 类SegmentIntance.java的实现描述：TODO 类实现描述
 * 
 * @author jiajie.yujj 2010-12-25 下午07:18:37
 */
public class SegmentInstance extends ComponentInstance implements Serializable {

    private static final long serialVersionUID = -2307992962779806227L;

    List<LayoutInstance>      layouts;

    public List<LayoutInstance> getLayouts() {
        return layouts;
    }

    public void setLayouts(List<LayoutInstance> layouts) {
        this.layouts = layouts;
    }

}
