package com.alibaba.fastjson.serializer;

import java.io.IOException;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeType;

public class CompositeDataSerializer implements ObjectSerializer {

	public final static CompositeDataSerializer instance = new CompositeDataSerializer();

	public void write(JSONSerializer serializer, Object object)
			throws IOException {
		CompositeData data = (CompositeData) object;

		SerializeWriter out = serializer.getWriter();

		out.write('{');
		boolean first = true;

		CompositeType type = data.getCompositeType();

		for (Object key : type.keySet()) {
			Object value = data.get((String) key);

			if (value == null) {
				if (!serializer.isEnabled(SerializerFeature.WriteMapNullValue)) {
					continue;
				}
			}

			if (!first) {
				out.write(',');
			}

			out.writeFieldName((String) key);

			serializer.write(value);

			first = false;
		}

		out.write('}');
	}

}
