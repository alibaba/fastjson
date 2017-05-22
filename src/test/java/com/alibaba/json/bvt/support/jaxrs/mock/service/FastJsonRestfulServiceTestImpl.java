/**
 * <p>Title: FastJsonRestfulServiceImpl.java</p>
 * <p>Description: FastJsonRestfulServiceImpl</p>
 * <p>Package: com.alibaba.json.bvt.support.jaxrs.mock.service</p>
 * <p>Company: www.github.com/DarkPhoenixs</p>
 * <p>Copyright: Dark Phoenixs (Open-Source Organization) 2016</p>
 */
package com.alibaba.json.bvt.support.jaxrs.mock.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.json.bvt.support.spring.mock.entity.FastJsonParentTestVO;
import com.alibaba.json.bvt.support.spring.mock.entity.FastJsonTestVO;

/**
 * <p>Title: FastJsonRestfulServiceImpl</p>
 * <p>Description: </p>
 *
 * @since 2016年4月20日
 * @author Victor.Zxy
 * @version 1.0
 */
@Service("fastJsonRestful")
public class FastJsonRestfulServiceTestImpl implements FastJsonRestfulServiceTest {

	@Override
	public JSONObject test1(FastJsonTestVO vo) {

		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("id", vo.getId());
		
		jsonObj.put("name", vo.getName());
		
		return jsonObj;
	}

	@Override
	public JSONObject test2(List<FastJsonParentTestVO> vos) {

		JSONObject jsonObj = new JSONObject();
		
		for (FastJsonParentTestVO fastJsonParentTestVO : vos) {
			
			jsonObj.put(fastJsonParentTestVO.getName(), fastJsonParentTestVO.getSonList().size());
		}
		
		return jsonObj;
	}
}
