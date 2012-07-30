package com.alibaba.fastjson.serializer;

import java.util.Collection;

/**
 * 键式上下文路径描述，与SerialContext不同，此不展示详细的路径表达，而只是描述从根到子节点的一条属性线
 * 如root[a(id,name),b(id,name)],表示id时使用serilContext表示为$[1].id或$[2].id；
 * 而使用linktedContext，则表示为id,忽略根对象以及在数组或列表中的序号
 */
public class SerialLinkedContext {

    private final SerialLinkedContext parent;

    private final Object        object;

    private final Object        fieldName;

	private transient String linkedPath;

    public SerialLinkedContext(SerialLinkedContext parent, Object object, Object fieldName){
        this.parent = parent;
        this.object = object;
        this.fieldName = fieldName;
    }

    public SerialLinkedContext getParent() {
        return parent;
    }

    public Object getObject() {
        return object;
    }

    public Object getFieldName() {
        return fieldName;
    }

	public String getLinkedPath() {
		if(linkedPath == null) {
			linkedPath = calculateLinkedPath();
		}
		return linkedPath;
	}

	private String calculateLinkedPath() {
		boolean isCollection = object instanceof Collection;
		if (parent == null) {
			return isCollection ? "" : String.valueOf(fieldName);
		}
		if(isCollection)
			return parent.getLinkedPath();
		return parent.getLinkedPath() + "." + fieldName;
	}

	public String toString() {
        return getLinkedPath();
    }
}
