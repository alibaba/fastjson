package com.alibaba.json.bvt.android;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class OnlyFieldTest extends TestCase {

    public void test_0() throws Exception {
        TestParcel parcel = new TestParcel();
        String text = JSON.toJSONString(parcel);

        JSON.parseObject(text, TestParcel.class);

    }

    public static interface Parcelable {

    }

    public static class ParcelCompany implements Parcelable {

        public int    entryAge    = 1;
        public String companyName = "";
        public String phoneNum    = "";
        public String address     = "";

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }

    }

    public static class TestParcel {

        public int           mAge      = 15;
        public int           mSex      = 0;
        public String        mName     = "";
        public String        mPhoneNum = "";
        public String        mAddress  = "";
        public ParcelCompany mCompany  = new ParcelCompany();
    }

  
}
