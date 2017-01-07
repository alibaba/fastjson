package com.alibaba.json.test.vans;

import com.alibaba.fastjson.annotation.JSONType;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xiaolin_kxl on 17/1/5.
 */
@JSONType(orders = {"name","uuid","matrix","visible","type","children","castShadow","receiveShadow","geometry"})
public class VansObjectChildren implements Serializable{
    public String name;
    public String uuid;
    public String type;
    public boolean visible;
    public String geometry;
    public boolean castShadow;
    public boolean receiveShadow;
    public float[] matrix;
    public ArrayList<VansObjectChildren> children;
}
