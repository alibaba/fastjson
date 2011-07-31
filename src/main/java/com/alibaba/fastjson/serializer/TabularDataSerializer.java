package com.alibaba.fastjson.serializer;

import java.io.IOException;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;

public class TabularDataSerializer implements ObjectSerializer {

    public final static TabularDataSerializer instance = new TabularDataSerializer();

    public void write(JSONSerializer serializer, Object object) throws IOException {
        TabularData data = (TabularData) object;

        SerializeWriter out = serializer.getWriter();

        out.write('{');
        out.writeFieldName("columns");
        serializer.write(data.getTabularType().getIndexNames());
        out.write(',');
        out.writeFieldName("rows");
        out.write("[");

        boolean first = true;
        for (Object value : data.values()) {

            if (!first) {
                out.write(',');
            }

            CompositeData row = (CompositeData) value;
            serializer.write(row.values());

            first = false;
        }

        out.write("]}");
    }

}
