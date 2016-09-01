/**
 * <p>Title: FastJsonControllerTest.java</p>
 * <p>Description: FastJsonControllerTest</p>
 * <p>Package: com.alibaba.json.bvt.support.spring.controller</p>
 * <p>Company: www.github.com/DarkPhoenixs</p>
 * <p>Copyright: Dark Phoenixs (Open-Source Organization) 2016</p>
 */
package com.alibaba.json.bvt.support.spring.mock.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.json.bvt.support.spring.mock.entity.FastJsonEnumTestVO;
import com.alibaba.json.bvt.support.spring.mock.entity.FastJsonGenericityTestVO;
import com.alibaba.json.bvt.support.spring.mock.entity.FastJsonParentTestVO;
import com.alibaba.json.bvt.support.spring.mock.entity.FastJsonTestVO;
import com.alibaba.json.test.entity.Company;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>Title: FastJsonControllerTest</p>
 * <p>Description: </p>
 *
 * @author Victor.Zxy
 * @version 1.0
 * @since 2016年4月20日
 */
@Controller
@RequestMapping("fastjson")
public class FastJsonControllerTest {

    @RequestMapping("test1")
    public
    @ResponseBody
    JSONObject test1(@RequestBody FastJsonTestVO vo) {

        JSONObject jsonObj = new JSONObject();

        jsonObj.put("id", vo.getId());

        jsonObj.put("name", vo.getName());

        return jsonObj;
    }

    @RequestMapping("test2")
    public
    @ResponseBody
    JSONObject test1(@RequestBody List<FastJsonParentTestVO> vos) {

        JSONObject jsonObj = new JSONObject();

        for (FastJsonParentTestVO fastJsonParentTestVO : vos) {

            jsonObj.put(fastJsonParentTestVO.getName(), fastJsonParentTestVO.getSonList().size());
        }

        return jsonObj;
    }

    @ResponseBody
    @RequestMapping(value = "test3", method = RequestMethod.POST)
    public Company test3(HttpServletRequest request, HttpServletResponse response) {
        Company company = new Company();
        return company;
    }

    @RequestMapping("test4")
    public
    @ResponseBody
    String test4(@RequestBody FastJsonGenericityTestVO<FastJsonTestVO> queryCondition) {

        return JSON.toJSONString(queryCondition);
    }

    @RequestMapping("test5")
    public
    @ResponseBody
    String test5(@RequestBody FastJsonEnumTestVO vo) {

        return JSON.toJSONString(vo);
    }

    @RequestMapping(value = "/test6", method = {RequestMethod.POST, RequestMethod.GET}, produces = {"text/plain", "application/*"})
    public
    @ResponseBody
    Object test6(@RequestParam long userId, @RequestParam boolean flag) {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("userId",userId);
        jsonObject.put("flag",flag);

        System.out.println(jsonObject.toJSONString());

        return jsonObject;
    }
}
