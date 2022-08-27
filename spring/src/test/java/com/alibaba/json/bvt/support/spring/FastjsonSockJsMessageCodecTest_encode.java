package com.alibaba.json.bvt.support.spring;

import org.junit.Assert;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import com.alibaba.fastjson.support.spring.FastjsonSockJsMessageCodec;

import junit.framework.TestCase;

public class FastjsonSockJsMessageCodecTest_encode extends TestCase {
    public void test_encode() throws Exception {
        FastjsonSockJsMessageCodec fastjsonCodec = new FastjsonSockJsMessageCodec();
        Jackson2SockJsMessageCodec jacksonCodec = new Jackson2SockJsMessageCodec();
        
        String v0 = "a0\"\0", v1 = "a1";
        String fastjsonResult = fastjsonCodec.encode(v0, v1);
        String jacksonResult = jacksonCodec.encode(v0, v1);
        Assert.assertEquals(jacksonResult, fastjsonResult);
    }
}
