package com.alibaba.json.test.a;



import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class JTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<User[]> list = new ArrayList<User[]>();
		User u1 = new User(1, "111111");
		User u2 = new User(2, "222222");
		User u3 = new User(3, "33333");
		User u4 = new User(4, "4444");
		list.add(new User[] { u1, u2 });
		list.add(new User[] { u3, u4 });
		Group clz = new Group();
		clz.setUulist(list);
		String json = JSON.toJSONString(clz);
		System.out.println(json);
		Group clz1 = JSON.parseObject(json, Group.class);
		System.out.println(clz1.getUulist().get(1)[1].getName());
	}

}
