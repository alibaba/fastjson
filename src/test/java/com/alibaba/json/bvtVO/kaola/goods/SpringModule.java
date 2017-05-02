package com.alibaba.json.bvtVO.kaola.goods;

import java.io.Serializable;

/**
 * 列表基类
 *
 * @author wang.lucai
 * @time 2015-06-01
 */
public class SpringModule implements Serializable {

    private static final long serialVersionUID = 229413824101298070L;

    public static final int STYLE_NO = -1;

    public static final int TYPE_GOODS = 0;

    public static final int TYPE_BRAND = 1;

    public static final int TYPE_SPRING = 2;

    public static final int TYPE_BANNER = 3;

    public static final int TYPE_SMALL_BANNER = 37;

    public static final int TYPE_FEED_LABEL = 4;

    public static final int TYPE_FIND_HOBBY = 5;

    public static final int TYPE_ACTIVITY = 6;

    public static final int TYPE_SMALL_IMAGE = 7;
    public static final int TYPE_MIDDLE_IMAGE = 36;
    public static final int TYPE_LARGE_IMAGE = 8;
    public static final int TYPE_HORIZONTAL_GOODS = 9;
    public static final int TYPE_HORIZONTAL_BRAND = 10;
    public static final int TYPE_HORIZONTAL_COUPON = 11;
    public static final int TYPE_HORIZONTAL_IMAGE = 12;
    public static final int TYPE_DOUBLE_IMAGE = 13;
    public static final int TYPE_LARGE_DOUBLE_IMAGE_WITH_VERTICAL_SPACE = 14;
    public static final int TYPE_FOUR_IMAGE = 15;
    public static final int TYPE_NEW_DISCOVERY = 16;
    public static final int TYPE_NEW_DISCOVERY_OLD = 17;
    public static final int TYPE_SECOND_KILL = 18;
    public static final int TYPE_SECOND_KILL_2 = 24;
    public static final int TYPE_GLOBAL_BUY = 19;
    public static final int TYPE_PHONE_ONLY = 20;
    public static final int TYPE_FOUR_COLUMN_NAVIGATION = 21;
    public static final int TYPE_DOUBLE_GOODS = 22;
    public static final int TYPE_SINGLE_GOODS = 35;
    public static final int TYPE_DISCOVER_TOP_LABEL = 23;
    public static final int TYPE_DISCOVER_CATEGORY = 25;
    public static final int TYPE_DISCOVER_INTEREST = 26;
    public static final int TYPE_ADVERTISE_BANNER = 27;
    public static final int TYPE_ALBUM_BANNER = 28;
    public static final int TYPE_DISCOVER_DISCUSS = 29;
    public static final int TYPE_LEFT_ONE_RIGHT_TOW = 30;
    public static final int TYPE_LARGE_FOUR_IMAGE = 31;
    public static final int TYPE_MOM_INFANT = 32;
    public static final int TYPE_ALBUM = 33;
    public static final int TYPE_MIDDLE_TAB = 34;
    public static final int TYPE_HORIZONTAL_RED_PACKET = 35;
    public static final int TYPE_FIVE_IMAGE = 38;
    public static final int TYPE_TODAY_NEW = 41;
    public static final int TYPE_COUNTRY = 39;
    public static final int TYPE_SEPARATOR = 40;
    public static final int TYPE_LARGE_DOUBLE_IMAGE = 46;
    public static final int TYPE_DISCOVERY_GOOD_PRICE = 47;
    public static final int TYPE_DISCOVERY_GOOD_THINGS = 48;
    public static final int TYPE_DISCOVERY_GOOD_ARTICLE = 49;
    public static final int TYPE_DISCOVERY_LIVE_BROADCAST = 50;
    public static final int TYPE_DISCOVERY_CATEGORY = 51;
    public static final int TYPE_DISCOVERY_ZDM_BANNER = 52;
    protected int springType;
    protected int styleType = STYLE_NO;
    protected String biMark;

    public void setSpringType(int springType) {
        this.springType = springType;
    }

    public int getSpringType() {
        return springType;
    }

    public int getStyleType() {
        return styleType;
    }

    public void setStyleType(int styleType) {
        this.styleType = styleType;
    }

    public String getBiMark() {
        return biMark;
    }

    public void setBiMark(String biMark) {
        this.biMark = biMark;
    }
}