package com.alibaba.fastjson.support.spring;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.socket.sockjs.frame.AbstractSockJsMessageCodec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class FastjsonSockJsMessageCodec extends AbstractSockJsMessageCodec {

    @Override
    public String[] decode(String content) throws IOException {
        return JSON.parseObject(content, String[].class);
    }

    @Override
    public String[] decodeInputStream(InputStream content) throws IOException {
        return JSON.parseObject(content, String[].class);
    }

    @Override
    protected char[] applyJsonQuoting(String content) {
        SerializeWriter out = new SerializeWriter();
        try {
            JSONSerializer serializer = new JSONSerializer(out);
            serializer.write(content);
            return out.toCharArrayForSpringWebSocket();
        } finally {
            out.close();
        }
    }

}
