package xss;

import com.alibaba.fastjson.annotation.JSONXSSFilter;

/**
 * Created by Jintao on 2015/11/3.
 */
public class TestObj {

    @JSONXSSFilter
    private String script;

    private String b;

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }
}
