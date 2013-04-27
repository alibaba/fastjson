package com.alibaba.json.test.entity.pagemodel;

import java.io.Serializable;
import java.util.List;

/**
 * TODO Comment of PageInstance
 * 
 * @author jiajie.yujj @ 2010-11-29 ����09:53:49
 */
public class PageInstance extends ComponentInstance implements Serializable {

    private static final long     serialVersionUID = 7392769933771342661L;

    private List<SegmentInstance> segments;

    public List<SegmentInstance> getSegments() {
        return segments;
    }

    public void setSegments(List<SegmentInstance> segments) {
        this.segments = segments;
    }

}
