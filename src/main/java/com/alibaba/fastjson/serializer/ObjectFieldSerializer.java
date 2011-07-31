/*
 * Copyright 1999-2101 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.FieldInfo;

/**
 * @author wenshao<szujobs@hotmail.com>
 */
public class ObjectFieldSerializer extends FieldSerializer {

	private ObjectSerializer fieldSerializer;
	private Class<?> runtimeFieldClass;
	private String format;

	public ObjectFieldSerializer(FieldInfo fieldInfo) {
		super(fieldInfo);

		JSONField annotation = fieldInfo.getAnnotation(JSONField.class);

		if (annotation != null) {
			format = annotation.format();

			if (format.trim().length() == 0) {
				format = null;
			}
		}
	}

	@Override
	public void writeProperty(JSONSerializer serializer, Object propertyValue)
			throws Exception {
		writePrefix(serializer);

		if (format != null) {
			serializer.writeWithFormat(propertyValue, format);
			return;
		}

		if (fieldSerializer == null) {

			if (propertyValue == null) {
				runtimeFieldClass = this.getMethod().getReturnType();
			} else {
				runtimeFieldClass = propertyValue.getClass();
			}

			fieldSerializer = serializer.getObjectWriter(runtimeFieldClass);
		}

		if (propertyValue == null) {
			fieldSerializer.write(serializer, null);
			return;
		}

		if (propertyValue.getClass() == runtimeFieldClass) {
			fieldSerializer.write(serializer, propertyValue);
			return;
		}

		serializer.write(propertyValue);
	}
}
