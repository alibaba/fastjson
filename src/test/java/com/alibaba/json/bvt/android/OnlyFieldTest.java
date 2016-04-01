package com.alibaba.json.bvt.android;

import com.alibaba.fastjson.JSON;

public class OnlyFieldTest {
    public void test_0 () throws Exception {
        TestParcel parcel = new TestParcel();
        String text = JSON.toJSONString(parcel);
        System.out.println(text);
        
        JSON.parseObject(text, TestParcel.class);
        
    }
    
    public static interface Parcelable {
    
    }
    
    public static class ParcelCompany implements Parcelable {

        public int entryAge = 1;
        public String companyName = "";
        public String phoneNum = "";
        public String address = "";
        
        public void setAddress(String address) {
            throw new UnsupportedOperationException();
        }
    }
    
    
    public static class TestParcel {
        public int mAge = 15;
        public int mSex = 0;
        public String mName = "";
        public String mPhoneNum = "";
        public String mAddress = "";
        public ParcelCompany mCompany = new ParcelCompany();
    }
    
    public static void main(String[] args) throws Exception {
        OnlyFieldTest test = new OnlyFieldTest();
        test.test_0();
        
        Thread.sleep(1000 * 1000);
    }
}
