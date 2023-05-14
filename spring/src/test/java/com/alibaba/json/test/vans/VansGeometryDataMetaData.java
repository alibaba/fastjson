package com.alibaba.json.test.vans;

import java.io.Serializable;

/**
 * Created by xiaolin_kxl on 16/12/28.
 */

public class VansGeometryDataMetaData implements Serializable {
    public int version;
    public int uvs;
    public int normals;
    public int faces;
    public int vertices;
    public String generator;

    public VansGeometryDataMetaData() {

    }
}

