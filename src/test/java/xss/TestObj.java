package xss;

import com.alibaba.fastjson.annotation.JSONXSSFilter;

/**
 * Created by Jintao on 2015/11/3.
 */
public class TestObj {

    @JSONXSSFilter
    private String a;

    private String b;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }
}
