/**
 * <p>Title: FastJsonControllerTest.java</p>
 * <p>Description: FastJsonControllerTest</p>
 * <p>Package: com.alibaba.json.bvt.support.spring.controller</p>
 * <p>Company: www.github.com/DarkPhoenixs</p>
 * <p>Copyright: Dark Phoenixs (Open-Source Organization) 2016</p>
 */
package com.alibaba.json.bvt.support.spring.mock.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.json.bvt.support.spring.mock.entity.FastJsonParentTestVO;
import com.alibaba.json.bvt.support.spring.mock.entity.FastJsonTestVO;
import com.alibaba.json.test.entity.Company;

/**
 * <p>Title: FastJsonControllerTest</p>
 * <p>Description: </p>
 *
 * @since 2016年4月20日
 * @author Victor.Zxy
 * @version 1.0
 */
@Controller
@RequestMapping("fastjson")
public class FastJsonControllerTest {

	@RequestMapping("test1")
	public @ResponseBody JSONObject test1(@RequestBody FastJsonTestVO vo) {

		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("id", vo.getId());
		
		jsonObj.put("name", vo.getName());
		
		return jsonObj;
	}

	@RequestMapping("test2")
	public @ResponseBody JSONObject test1(@RequestBody List<FastJsonParentTestVO> vos) {

		JSONObject jsonObj = new JSONObject();
		
		for (FastJsonParentTestVO fastJsonParentTestVO : vos) {
			
			jsonObj.put(fastJsonParentTestVO.getName(), fastJsonParentTestVO.getSonList().size());
		}
		
		return jsonObj;
	}
	
	@ResponseBody
    @RequestMapping(value = "test3", method = RequestMethod.POST)
    public Company test3(HttpServletRequest request, HttpServletResponse response) {
        Company company=new Company();
        return company;
    }
}
