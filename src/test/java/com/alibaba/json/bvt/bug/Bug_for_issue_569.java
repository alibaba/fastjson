package com.alibaba.json.bvt.bug;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.json.bvt.bug.Bug_for_issue_569.LoginResponse.Body;
import com.alibaba.json.bvt.bug.Bug_for_issue_569.LoginResponse.MemberInfo;

import junit.framework.TestCase;

public class Bug_for_issue_569 extends TestCase {

    public void test_for_issue() throws Exception {
        LoginResponse loginResp = new LoginResponse();
        loginResp.response = new Response<LoginResponse.Body>();
        loginResp.response.content = new Body();
        loginResp.response.content.setMemberinfo(new MemberInfo());
        loginResp.response.content.getMemberinfo().name = "ding102992";
        loginResp.response.content.getMemberinfo().email = "ding102992@github.com";

        String text = JSON.toJSONString(loginResp);

        LoginResponse loginResp2 = JSON.parseObject(text, LoginResponse.class);
        
        Assert.assertEquals(loginResp.response //
                                     .getContent() //
                                     .getMemberinfo().name, //
                            loginResp2.response //
                                      .getContent() //
                                      .getMemberinfo().name);
        Assert.assertEquals(loginResp.response //
                                     .getContent().getMemberinfo().email, //
                            loginResp2.response.getContent().getMemberinfo().email);

    }

    public static class BaseResponse<T> {

        public Response<T> response;

    
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

            public String name;
            public String email;
            /*
             * 省略Getter,Setter
             */
        }
    }
}
