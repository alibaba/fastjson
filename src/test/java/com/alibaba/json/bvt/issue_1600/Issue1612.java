package com.alibaba.json.bvt.issue_1600;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.junit.Test;

/**
 * <p>Title: Issue1612</p>
 * <p>Description: </p>
 *
 * @author Victor
 * @version 1.0
 * @since 2017/11/27
 */
public class Issue1612 {

    @Test
    public void test() {

        RegResponse<User> userRegResponse = testFastJson(User.class);

        User user = userRegResponse.getResult();
        System.out.println(user);

    }

    public static <T> RegResponse<T> testFastJson(Class<T> clasz) {

        //把body解析成一个对象
        String body = "{\"retCode\":\"200\", \"result\":{\"name\":\"Zhangsan\",\"password\":\"123\"}}";

        return JSON.parseObject(body, new TypeReference<RegResponse<T>>(clasz) {});
    }
}

class RegResponse<T> {

    private String retCode;
    private String retDesc;
    private T result;

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetDesc() {
        return retDesc;
    }

    public void setRetDesc(String retDesc) {
        this.retDesc = retDesc;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RegResponse{" +
                "retCode='" + retCode + '\'' +
                ", retDesc='" + retDesc + '\'' +
                ", result=" + result +
                '}';
    }
}

class User {

    public User(){}
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
