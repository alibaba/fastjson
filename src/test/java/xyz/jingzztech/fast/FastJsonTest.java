/**
 * 
 */
package xyz.jingzztech.fast;

import com.alibaba.fastjson.JSONObject;

/**
 * @author jingzz
 * @time 2016年3月30日 下午1:53:06
 * @name fastjson/xyz.jingzztech.fast.FastJsonTest
 * @since 2016年3月30日 下午1:53:06
 */
public class FastJsonTest {
	
	public static void main(String[] args) {
		DemoUser user = new DemoUser();
		user.setId("hehe");
		user.setName("xixixi");
		user.content="sdfsdf";
		
		System.out.println(JSONObject.toJSONString(user));
	}
}
