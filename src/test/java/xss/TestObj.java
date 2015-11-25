package xss;

import com.alibaba.fastjson.annotation.JSONXSSFilter;

/**
 * Created by Jintao on 2015/11/3.
 */
public class TestObj {

    @JSONXSSFilter
    private String script;

    @JSONXSSFilter
    private String newLine;

    private String b;

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getNewLine() {
        return newLine;
    }

    public void setNewLine(String newLine) {
        this.newLine = newLine;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }
}
