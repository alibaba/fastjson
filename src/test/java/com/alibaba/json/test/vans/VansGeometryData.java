package com.alibaba.json.test.vans;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;

import java.io.Serializable;
import java.util.ArrayList;

@JSONType(orders = {"uvs","metadata","normals","name","faces","vertices"})
public class VansGeometryData implements Serializable{
    public float[][] uvs;

    @JSONField(name = "metadata")
    public VansGeometryDataMetaData metaData;

    public float[] normals;
    public String name;
    public int[] faces;
    public float[] vertices;

    public VansGeometryData(){

    }
}
