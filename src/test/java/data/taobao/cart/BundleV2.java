package data.taobao.cart;

import data.taobao.cart.BundleV2.BundleV2Fields;

public class BundleV2 extends CartObject<BundleV2Fields>{
    public static class BundleV2Fields {
        public boolean valid;
        public String bundleId;
    }
}
