package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

public class JSONObjectCodec implements ObjectSerializer {
	public final static JSONObjectCodec instance = new JSONObjectCodec();

	@Override
	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
			throws IOException {
		SerializeWriter out = serializer.out;
		MapSerializer mapSerializer = MapSerializer.instance;

		try {
			Field mapField = object.getClass().getDeclaredField("map");
			if (Modifier.isPrivate(mapField.getModifiers())) {
				mapField.setAccessible(true);
			}

			Object map = mapField.get(object);
			mapSerializer.write(serializer, map, fieldName, fieldType, features);

		} catch (Exception e) {
			out.writeNull();
		}
	}
}
