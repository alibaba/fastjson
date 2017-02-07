package com.alibaba.json.test.vans;

import com.alibaba.fastjson.annotation.JSONType;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xiaolin_kxl on 17/1/5.
 */
@JSONType(orders = {"uuid","type","matrix","children"})
public class VansObject implements Serializable {
    public String uuid;
    public String type;
    public ArrayList<VansObjectChildren> children;
    public float[] matrix;
}
