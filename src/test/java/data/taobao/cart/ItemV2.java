package data.taobao.cart;

import java.math.BigDecimal;
import java.util.List;

import data.taobao.cart.ItemV2.ItemV2Fields;

public class ItemV2 extends CartObject<ItemV2Fields> {

    public static class ItemV2Fields {

        public boolean      valid;
        public boolean      canBatchRemove;
        public String       toBuy;
        public String       settlement;
        public long         cartId;
        public String       bundleType;
        public String       exclude;
        public String       pic;
        public long         itemId;
        public SKU          sku;
        public String       url;
        public String       mutex;
        public String       title;
        public boolean      showCheckBox;
        public long         sellerId;
        public List<String> operate;
        public String       bundleId;
        public Pay          pay;
        public Quantity     quantity;
        public String       itemRecParamId;
        public boolean      checked;

    }

    public static class SKU {

        public String  title;
        public String  status;
        public long    skuId;
        public long    areaId;
        public boolean editable;
    }

    public static class Pay {

        public String     totalTitle;
        public BigDecimal total;
        public String     originTitle;
        public BigDecimal origin;
        public String     nowTitle;
        public BigDecimal now;
    }

    public static class Quantity {

        public int     multiple;
        public int     min;
        public int     quantity;
        public int     max;
        public boolean editable;
    }
}
