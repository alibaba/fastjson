package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class InetSocketAddressSerializer implements ObjectSerializer {

    public static InetSocketAddressSerializer instance = new InetSocketAddressSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        if (object == null) {
            serializer.writeNull();
            return;
        }

        SerializeWriter out = serializer.getWriter();
        InetSocketAddress address = (InetSocketAddress) object;

        InetAddress inetAddress = address.getAddress();

        out.write('{');
        if (inetAddress != null) {
            out.writeFieldName("address");
            serializer.write(inetAddress);
            out.write(',');
        }
        out.writeFieldName("port");
        out.writeInt(address.getPort());
        out.write('}');
    }
}
