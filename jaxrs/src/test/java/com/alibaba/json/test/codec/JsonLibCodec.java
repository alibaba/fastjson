package com.alibaba.json.test.codec;

import java.io.OutputStream;
import java.util.Collection;

public class JsonLibCodec implements Codec {

    public String getName() {
        return "json-lib";
    }

    public <T> T decodeObject(String text, Class<T> clazz) throws Exception {
        return (T) net.sf.json.JSONObject.fromObject(text);
    }

    public <T> Collection<T> decodeArray(String text, Class<T> clazz) throws Exception {
        throw new UnsupportedOperationException();
    }

    public Object decodeObject(String text) throws Exception {
        return net.sf.json.JSONObject.fromObject(text);
    }

    public Object decode(String text) throws Exception {
        if (text.charAt(0) == '[') {
            return net.sf.json.JSONArray.fromObject(text);
        }
        return net.sf.json.JSONObject.fromObject(text);
        // return net.sf.json.JSONArray.fromObject(text);
    }

    public String encode(Object object) throws Exception {
        return net.sf.json.JSONObject.fromObject(object).toString();
    }
    

    public <T> T decodeObject(byte[] input, Class<T> clazz) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] encodeToBytes(Object object) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void encode(OutputStream out, Object object) throws Exception {
        out.write(encodeToBytes(object));
    }
}
