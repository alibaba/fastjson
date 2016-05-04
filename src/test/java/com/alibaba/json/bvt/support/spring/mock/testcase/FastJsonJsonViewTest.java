/**
 * <p>Title: FastJsonJsonViewTest.java</p>
 * <p>Description: FastJsonJsonViewTest</p>
 * <p>Package: com.alibaba.json.bvt.support.spring.mock.testcase</p>
 * <p>Company: www.github.com/DarkPhoenixs</p>
 * <p>Copyright: Dark Phoenixs (Open-Source Organization) 2016</p>
 */
package com.alibaba.json.bvt.support.spring.mock.testcase;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;

/**
 * <p>Title: FastJsonJsonViewTest</p>
 * <p>Description: </p>
 *
 * @since 2016年4月20日
 * @author Victor.Zxy
 * @version 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "classpath*:/config/applicationContext-mvc2.xml" })
public class FastJsonJsonViewTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = webAppContextSetup(this.wac).build();
	}

	@Test
	public void test1() throws Exception {
		
		JSONObject json = new JSONObject();
		
		json.put("id", 123);
		
		json.put("name", "哈哈哈");
		
		mockMvc.perform(
				(post("/fastjson/test1").characterEncoding("UTF-8").content(json.toJSONString()).contentType(MediaType.APPLICATION_JSON)
						))
//		.andExpect(status().isOk())
				.andDo(print());
	}
	
	@Test
	public void test2() throws Exception {
		
		String jsonStr = "[{\"name\":\"p1\",\"sonList\":[{\"name\":\"s1\"}]},{\"name\":\"p2\",\"sonList\":[{\"name\":\"s2\"},{\"name\":\"s3\"}]}]";
		
		mockMvc.perform(
				(post("/fastjson/test2").characterEncoding("UTF-8").content(jsonStr).contentType(MediaType.APPLICATION_JSON)
						))
//		.andExpect(status().isOk())
				.andDo(print());
	}
}
