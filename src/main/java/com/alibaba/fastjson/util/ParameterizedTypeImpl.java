package com.alibaba.fastjson.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

public class ParameterizedTypeImpl implements ParameterizedType {
	private final Type[] actualTypeArguments;
	private final Type ownerType;
	private final Type rawType;
	private int hash;

	public ParameterizedTypeImpl(Type[] actualTypeArguments, Type ownerType,
			Type rawType) {
		super();
		if (actualTypeArguments == null || actualTypeArguments.length == 0
				|| rawType == null)
			throw new IllegalArgumentException();
		this.actualTypeArguments = actualTypeArguments;
		this.ownerType = ownerType;
		this.rawType = rawType;

		hash = 31 + (ownerType != null ? ownerType.hashCode() : 0);
		hash = 31 * hash + rawType.hashCode();
		hash = 31 * hash + Arrays.hashCode(actualTypeArguments);
	}

	public Type[] getActualTypeArguments() {
		return actualTypeArguments;
	}

	public Type getOwnerType() {
		return ownerType;
	}

	public Type getRawType() {
		return rawType;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ParameterizedType)) {
			return false;
		}
		ParameterizedType other = (ParameterizedType) obj;
		if (!Arrays.equals(actualTypeArguments, other.getActualTypeArguments())) {
			return false;
		}
		if (ownerType == null) {
			if (other.getOwnerType() != null) {
				return false;
			}
		} else if (!ownerType.equals(other.getOwnerType())) {
			return false;
		}
		if (rawType == null) {
			if (other.getRawType() != null) {
				return false;
			}
		} else if (!rawType.equals(other.getRawType())) {
			return false;
		}
		return true;
	}
}
