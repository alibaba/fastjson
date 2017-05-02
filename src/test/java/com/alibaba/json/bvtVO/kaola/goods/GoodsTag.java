package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;

/**
 * Created by hzfengboyang on 2016/4/28.
 */
public class GoodsTag implements Serializable {
    private static final long serialVersionUID = -177805472204903828L;
    private  String icon;                //图标url
    private  String name;               //tag名称

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
