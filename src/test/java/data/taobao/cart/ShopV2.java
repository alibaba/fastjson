package data.taobao.cart;

import data.taobao.cart.ShopV2.ShopV2Fields;

public class ShopV2 extends CartObject<ShopV2Fields> {
    public static class ShopV2Fields {
        public String title;
        public boolean is11;
        public long sellerId;
        public long shopId;
        public String sType;
        public boolean checked;
        public String seller;
        public String url;
        public boolean hasBonus;
    }
}
