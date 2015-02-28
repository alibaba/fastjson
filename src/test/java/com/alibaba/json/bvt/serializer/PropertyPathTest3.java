package com.alibaba.json.bvt.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.alibaba.fastjson.serializer.SerialContext;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyPathTest3 extends TestCase {

	/** 测试只输出子集合中的特定属性 */
	public void test_path() throws Exception {
		Person p1 = new Person();
		p1.setId(100);

		Person c1 = new Person();
		c1.setId(1000);

		Person c2 = new Person();
		c2.setId(2000);

		p1.getChildren().add(c1);
		p1.getChildren().add(c2);
		//只输出children.id以及根上的id
		String s = JSON.toJSONString(p1, new MyPropertyPreFilter(new String[] {"children.id", "id"}));

		Assert.assertEquals("{\"children\":[{\"id\":1000},{\"id\":2000}],\"id\":100}", s);
	}

	/** 测试只输出子字段map中的特定属性 */
	public void test_path2() throws Exception {
		Person2 p1 = new Person2();
		p1.setId(1);
		Map<String, String> infoMap = new HashMap<String, String>();
		infoMap.put("name", "李三");
		infoMap.put("height", "168");
		p1.setInfoMap(infoMap);
		//只输出infoMap.name
		String s = JSON.toJSONString(p1, new MyPropertyPreFilter(new String[] {"infoMap.name"}));
		Assert.assertEquals("{\"infoMap\":{\"name\":\"李三\"}}", s);
	}

	public static class MyPropertyPreFilter implements PropertyPreFilter {
		String[] onlyProperties;

		public MyPropertyPreFilter(String[] onlyProperties) {
			this.onlyProperties = onlyProperties;
		}

		private static boolean containInclude(String[] ss, String s) {
			if(ss == null || ss.length == 0 || s == null)
				return false;
			for(String st : ss)
				if(st.startsWith(s))
					return true;
			return false;
		}

		public boolean apply(JSONSerializer serializer, Object source, String name) {
			SerialContext nowContext = new SerialContext(serializer.getContext(), source, name, 0, 0);
			String nowPath = getLinkedPath(nowContext);
			System.out.println("path->" + nowPath);
			//只输出children.id
			return containInclude(onlyProperties, nowPath);
		}

	}

	/** 输出结果 类似a.b.c.d等格式，忽略[] */
	private static String getLinkedPath(SerialContext serialContext) {
		//这里有点bad smell，即要考虑parent为null,又要考虑fieldName为null，且对collection判断只能从fieldName，而不能从object入手
		boolean isCollection = serialContext.getFieldName() instanceof Integer;
		boolean isFieldNameNull = serialContext.getFieldName() == null;
		if(serialContext.getParent() == null)
			return isCollection ? "" : isFieldNameNull ? "" : String.valueOf(serialContext.getFieldName());
		String parentLinkedPath = getLinkedPath(serialContext.getParent());
		if(isCollection || isFieldNameNull)
			return parentLinkedPath;
		return
			parentLinkedPath.length() == 0 ? String.valueOf(serialContext.getFieldName()) :
				parentLinkedPath + "." + serialContext.getFieldName();
	}

	public static class Person {

		private int id;
		private int id2;

		private List<Person> children = new ArrayList<Person>();

		public int getId2() {
			return id2;
		}

		public void setId2(int id2) {
			this.id2 = id2;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public List<Person> getChildren() {
			return children;
		}

		public void setChildren(List<Person> children) {
			this.children = children;
		}

	}

	public static class Person2 {
		private int id;
		private Map<String, String> infoMap;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public Map<String, String> getInfoMap() {
			return infoMap;
		}

		public void setInfoMap(Map<String, String> infoMap) {
			this.infoMap = infoMap;
		}
	}
}
