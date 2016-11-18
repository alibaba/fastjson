package com.alibaba.json.bvtVO;

import java.util.ArrayList;

/**
 * Created by wenshao on 2016/11/18.
 */
public class Issue900_CurrencyListResult {
    public static final int    VERSION = 1; // cache version, 如果结构改变, 需要同步修改版本号

    // 可支付币种列表
    private ArrayList<String> payCurrencyList = new ArrayList<String>();

    public ArrayList<String> getPayCurrencyList() {
        return payCurrencyList;
    }
}
