package com.alibaba.json.test.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.test.bug.Bug_for_issue_569.LoginResponse.Body;
import com.alibaba.json.test.bug.Bug_for_issue_569.LoginResponse.MemberInfo;

import junit.framework.TestCase;

public class Bug_for_issue_569 extends TestCase {

    public void test_for_issue() throws Exception {
        LoginResponse loginResp = new LoginResponse();
        loginResp.setResponse(new Response<LoginResponse.Body>());
        loginResp.getResponse().setContent(new Body());
        loginResp.getResponse().getContent().setMemberinfo(new MemberInfo());
        loginResp.getResponse().getContent().getMemberinfo().name = "ding102992";
        loginResp.getResponse().getContent().getMemberinfo().email = "ding102992@github.com";

        String text = JSON.toJSONString(loginResp);

        LoginResponse loginResp2 = JSON.parseObject(text, LoginResponse.class);

        Assert.assertEquals(loginResp.getResponse() //
                                     .getContent() //
                                     .getMemberinfo().name, //
                            loginResp2.getResponse() //
                                      .getContent() //
                                      .getMemberinfo().name);
        Assert.assertEquals(loginResp.getResponse() //
                                     .getContent().getMemberinfo().email, //
                            loginResp2.getResponse().getContent().getMemberinfo().email);

    }

    public static class BaseResponse<T> {

        private Response<T> response;

        public Response<T> getResponse() {
            return response;
        }

        public void setResponse(Response<T> response) {
            this.response = response;
        }


    
    }
    
    public static class Response<T> {

        private T content;

        public T getContent() {
            return content;
        }

        public void setContent(T content) {
            this.content = content;
        }

    }

    public static class LoginResponse extends BaseResponse<LoginResponse.Body> {

        public static class Body {

            private MemberInfo memberinfo;

            public MemberInfo getMemberinfo() {
                return memberinfo;
            }

            public void setMemberinfo(MemberInfo memberinfo) {
                this.memberinfo = memberinfo;
            }
        }

        public static class MemberInfo {

            private String name;
            private String email;
            /*
             * 省略Getter,Setter
             */
        }
    }
}
