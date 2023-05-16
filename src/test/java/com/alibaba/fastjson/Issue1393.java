//CS304 Issue link: https://github.com/alibaba/fastjson/issues/1393
package com.alibaba.fastjson;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class Issue1393 {

    /**
     * reproduce the issue
     **/
    @Test
    public void reproduce(){
        String jsonText;
        jsonText = "{\"code\":\"SUCCESS\",\"id\":1}";
        assertEquals("correct enum",jsonText,JSON.parseObject(jsonText, Result.class).toString());
        jsonText = "{\"code\":\"FAILURE\",\"id\":2}";
        assertEquals("correct enum",jsonText,JSON.parseObject(jsonText, Result.class).toString());
        jsonText = "{\"code\":\"ERROR\",\"id\":3}";
        assertEquals("correct enum",jsonText,JSON.parseObject(jsonText, Result.class).toString());
    }

    /**
     * test when enumCheck is false
     **/
    @Test
    public void testUnchecked(){
        String jsonText;
        jsonText = "{\"code\":\"SUCCESS\",\"id\":1}";
        assertEquals("correct enum",jsonText,JSON.parseObject(jsonText, Result2.class,false).toString());
        jsonText = "{\"code\":\"ERROR\",\"id\":2}";
        assertEquals("incorrect enum","{\"id\":2}",JSON.parseObject(jsonText, Result2.class,false).toString());
        jsonText = "{\"code\":\"SUCCESS\",\"id\":3,\"state\":\"CHECKED\"}";
        assertEquals("correct enum", jsonText,JSON.parseObject(jsonText, Result2.class,false).toString());
        jsonText = "{\"code\":\"SUCCESS\",\"id\":4,\"state\":\"UNKNOWN\"}";
        assertEquals("incorrect enum","{\"code\":\"SUCCESS\",\"id\":4}",JSON.parseObject(jsonText, Result2.class,false).toString());
    }

    /**
     * test when enumCheck is true
     **/
    @Test
    public void testChecked(){
        String jsonText;
        jsonText = "{\"code\":\"SUCCESS\",\"id\":1}";
        assertEquals("correct enum",jsonText,JSON.parseObject(jsonText, Result2.class,true).toString());
        jsonText = "{\"code\":\"FAILURE\",\"id\":1}";
        assertEquals("correct enum",jsonText,JSON.parseObject(jsonText, Result2.class,true).toString());
        jsonText = "{\"code\":\"ERROR\",\"id\":2}";
        try {
            JSON.parseObject(jsonText, Result2.class,true);
            fail();
        }
        catch (JSONException e){
            assertEquals("error message","Enum value ERROR does not exist in class com.alibaba.fastjson.Code.",e.getMessage());
        }
        jsonText = "{\"code\":\"SUCCESS\",\"id\":3,\"state\":\"CHECKED\"}";
        assertEquals("correct enum", jsonText,JSON.parseObject(jsonText, Result2.class,true).toString());
        jsonText = "{\"code\":\"SUCCESS\",\"id\":4,\"state\":\"UNKNOWN\"}";
        try {
            JSON.parseObject(jsonText, Result2.class,true);
            fail();
        }
        catch (JSONException e){
            assertEquals("error message","Enum value UNKNOWN does not exist in class com.alibaba.fastjson.State.",e.getMessage());
        }
        jsonText = "{\"code\":\"ERROR\",\"id\":5,\"state\":\"UNKNOWN\"}";
        try {
            JSON.parseObject(jsonText, Result2.class,true);
            fail();
        }
        catch (JSONException e){
            assertEquals("error message","Enum value ERROR does not exist in class com.alibaba.fastjson.Code.",e.getMessage());
        }
    }
}
enum Code{
    SUCCESS, FAILURE
}
class Result{
    private int id;
    private Code code;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Code getCode() {
        return code;
    }
    public void setCode(Code code) {
        this.code = code;
    }
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
class Result2{
    private int id;
    private Code code;
    private State state;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Code getCode() {
        return code;
    }
    public void setCode(Code code) {
        this.code = code;
    }
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
enum State{
    CHECKED(1), UNCHECKED(0);
    State(int a){
    }
}