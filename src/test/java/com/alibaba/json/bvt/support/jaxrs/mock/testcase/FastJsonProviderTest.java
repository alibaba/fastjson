/**
 * <p>Title: TestIssue885.java</p>
 * <p>Description: TestIssue885</p>
 * <p>Package: com.alibaba.json.bvt.support.jaxrs.mock.testcase</p>
 * <p>Company: www.github.com/DarkPhoenixs</p>
 * <p>Copyright: Dark Phoenixs (Open-Source Organization) 2016</p>
 */
package com.alibaba.json.bvt.support.jaxrs.mock.testcase;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.ContextLoaderListener;

import com.alibaba.fastjson.JSONObject;

/**
 * <p>Title: TestIssue885</p>
 * <p>Description: </p>
 *
 * @since 2016年4月20日
 * @author Victor.Zxy
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class FastJsonProviderTest {

	public final String REST_SERVICE_URL = "http://localhost:8088/rest";

	static {

		Server server = new Server(8088);

		// Register and map the dispatcher servlet
		final ServletHolder servletHolder = new ServletHolder(new CXFServlet());
		final ServletContextHandler context = new ServletContextHandler();
		context.setContextPath("/");
		context.addServlet(servletHolder, "/rest/*");
		context.addEventListener(new ContextLoaderListener());
		context.setInitParameter(
				"contextConfigLocation",
				"classpath*:/config/applicationContext-rest.xml");
		server.setHandler(context);
		try {
			server.start();
//			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test0() throws Exception {
		
		JSONObject json = new JSONObject();
		
		json.put("id", 123);
		
		json.put("name", "哈哈哈");
		
		WebClient client = WebClient.create(REST_SERVICE_URL);
		
		Response response = client.path("/fastjson/test1").accept("application/json").type("application/json; charset=UTF-8").post(json.toJSONString());
		
		System.out.println(response.readEntity(String.class));
	}
	
	@Test
	public void test1() throws Exception {
		
		JSONObject json = new JSONObject();
		
		json.put("id", 123);
		
		json.put("name", "哈哈哈");
		
		WebClient client = WebClient.create(REST_SERVICE_URL);
		
		Response response = client.path("/fastjson/test1").replaceQuery("pretty").accept("application/json").type("application/json; charset=UTF-8").post(json.toJSONString());
		
		System.out.println(response.readEntity(String.class));
	}
	
	@Test
	public void test2() throws Exception {
		
		String jsonStr = "[{\"name\":\"p1\",\"sonList\":[{\"name\":\"s1\"}]},{\"name\":\"p2\",\"sonList\":[{\"name\":\"s2\"},{\"name\":\"s3\"}]}]";
		
		WebClient client = WebClient.create(REST_SERVICE_URL);
		
		Response response = client.path("/fastjson/test2").accept("application/json").type("application/json; charset=UTF-8").post(jsonStr);
		
		System.out.println(response.readEntity(String.class));
	}
}
