package data.taobao.puti.model;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * 卡槽
 * */
//@JSONType(orders={"title", "imageUrl", "targetUrl", "extra", "bizType", "extras", // 
//                  "trackParam", "id", "trackParamProps", //
//                  "exposure", "valid"
//                  })
public class Item  {

    public List<Text> title; // 标题
    public List<Image> imageUrl; // 显示图片
    public String targetUrl; // 跳转页面的导航URL
    public String extra;
    public List<Extra> extras;
    public String bizType;
    public Map<String, String>  trackParam;
    public String  id;
	public Properties trackParamProps;
	public Boolean exposure;
	public Boolean valid;
	
	public int shopId;
}
