package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;
import java.util.List;

/**
 * 详情页色卡选择数据
 * Created by xinyang on 2015/12/28.
 */
public class GoodsColorSelection implements Serializable {
    private static final long serialVersionUID = 346360867017343436L;

    private List<GoodsColorImages> selections;

    public List<GoodsColorImages> getSelections() {
        return selections;
    }

    public void setSelections(List<GoodsColorImages> selections) {
        this.selections = selections;
    }

    public static class GoodsColorImages implements Serializable {

        private static final long serialVersionUID = -4913171515925595848L;

        private String breviary;
        private List<String> details;

        public String getBreviary() {
            return breviary;
        }

        public void setBreviary(String breviary) {
            this.breviary = breviary;
        }

        public List<String> getDetails() {
            return details;
        }

        public void setDetails(List<String> details) {
            this.details = details;
        }
    }
}
