package com.alibaba.json.bvtVO;

/**
 * Created by wenshao on 10/02/2017.
 */
public interface IntEnum<E extends Enum<E>> {
    int getCode();
}