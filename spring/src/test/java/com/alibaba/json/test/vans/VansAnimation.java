package com.alibaba.json.test.vans;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xiaolin_kxl on 17/1/5.
 */
public class VansAnimation implements Serializable{
    public String fps;
    public String name;
    public ArrayList<String> tracks;

    public VansAnimation(){

    }
}