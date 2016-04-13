package data.taobao.cart;

import java.util.List;

import data.taobao.cart.Promotion.PromotionFields;

public class Promotion extends CartObject<PromotionFields> {
    public static class PromotionFields {
        public String promotionType;
        public List<String> titles;
    }
}
