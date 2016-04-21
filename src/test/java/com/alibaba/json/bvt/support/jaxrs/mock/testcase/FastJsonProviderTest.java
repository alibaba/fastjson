/**
 * <p>Title: FastJsonProviderTest.java</p>
 * <p>Description: FastJsonProviderTest</p>
 * <p>Package: com.alibaba.json.bvt.support.jaxrs.mock.testcase</p>
 * <p>Company: www.github.com/DarkPhoenixs</p>
 * <p>Copyright: Dark Phoenixs (Open-Source Organization) 2016</p>
 */
package com.alibaba.json.bvt.support.jaxrs.mock.testcase;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;

import com.alibaba.fastjson.JSONObject;

/**
 * <p>Title: FastJsonProviderTest</p>
 * <p>Description: Restfull Test</p>
 *
 * @since 2016年4月20日
 * @author Victor.Zxy
 * @version 1.0
 */
public class FastJsonProviderTest {

	public static final String REST_SERVICE_URL = "http://localhost:8088/rest";

	public static void main(String[] args) throws Exception {
		
		test1();
		
//		test2();
	} 
	
	public static void test1() throws Exception {
		
		JSONObject json = new JSONObject();
		
		json.put("id", 123);
		
		json.put("name", "哈哈哈");
		
		WebClient client = WebClient.create(REST_SERVICE_URL);
		
		Response response = client.path("/fastjson/test1").accept("application/json").type("application/json; charset=UTF-8").post(json.toJSONString());
		
		System.out.println(response.readEntity(String.class));
	}
	
	public static void test2() throws Exception {
		
		String jsonStr = "[{\"name\":\"p1\",\"sonList\":[{\"name\":\"s1\"}]},{\"name\":\"p2\",\"sonList\":[{\"name\":\"s2\"},{\"name\":\"s3\"}]}]";
		
		WebClient client = WebClient.create(REST_SERVICE_URL);
		
		Response response = client.path("/fastjson/test2").accept("application/json").type("application/json; charset=UTF-8").post(jsonStr);
		
		System.out.println(response.readEntity(String.class));
	}
}
