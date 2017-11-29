/**
 * <p>Title: FastJsonRestfulService.java</p>
 * <p>Description: FastJsonRestfulService</p>
 * <p>Package: com.alibaba.json.bvt.support.jaxrs.mock.service</p>
 * <p>Company: www.github.com/DarkPhoenixs</p>
 * <p>Copyright: Dark Phoenixs (Open-Source Organization) 2016</p>
 */
package com.alibaba.json.bvt.support.jaxrs.mock.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.json.bvt.support.spring.mock.entity.FastJsonParentTestVO;
import com.alibaba.json.bvt.support.spring.mock.entity.FastJsonTestVO;

/**
 * <p>Title: FastJsonRestfulService</p>
 * <p>Description: </p>
 *
 * @since 2016年4月20日
 * @author Victor.Zxy
 * @version 1.0
 */
@Path("fastjson")
public interface FastJsonRestfulServiceTest {

	@POST
	@Path("/test1")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject test1(FastJsonTestVO vo);
	
	@POST
	@Path("/test2")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject test2(List<FastJsonParentTestVO> vos);
}
