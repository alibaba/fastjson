package data.taobao.cart;

import data.taobao.cart.Banner.BannerFields;

public class Banner extends CartObject<BannerFields>{

    public static class BannerFields {
        public String text;
        public String textColor;
        public String iconUrl;
        public int textCloseDays;
        public String pic;
        public String textBgColor;
        public String url;
    }

}
