/**
 * 
 */
package xyz.jingzztech.fast;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author jingzz
 * @time 2016年3月30日 下午1:53:14
 * @name fastjson/xyz.jingzztech.fast.DemoUser
 * @since 2016年3月30日 下午1:53:14
 */
public class DemoUser {
	private String name;
	
	private String id;
	
	@JSONField(serialize=true)
	public String content;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "DemoUser [name=" + name + ", id=" + id + "]";
	}
	
}
