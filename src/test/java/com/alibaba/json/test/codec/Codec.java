package com.alibaba.json.test.codec;

import java.util.Collection;

public interface Codec {

    String getName();

    <T> T decodeObject(String text, Class<T> clazz) throws Exception;

    <T> T decodeObject(byte[] input, Class<T> clazz) throws Exception;

    <T> Collection<T> decodeArray(String text, Class<T> clazz) throws Exception;

    Object decodeObject(String text) throws Exception;

    Object decode(String text) throws Exception;

    String encode(Object object) throws Exception;
}
