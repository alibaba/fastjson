/** Created by flym at 12-7-29 */
package com.alibaba.fastjson.serializer;

/**
 * 延迟对象，表示相应的对象信息将被延迟求值。
 * 真实数据将在被调用getValue时求值，因此在filter中，仅关心name或object时，可使用此对象
 *
 * @author flym
 */
public interface DelayObject<T> {
	/** 获取真实对象 */
	T getValue();
}
