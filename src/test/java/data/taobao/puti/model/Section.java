package data.taobao.puti.model;


import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONType;

//@JSONType(orders={"tracked", "template", "templateUrl", "version", "hideOnError" //
//                  , "items", "type", "group", "sections", "way" //
//                  , "id", "trackParam", "preload", "deleteId", "valid"})
public class Section{
    public  boolean tracked;

    public String template;
    public String templateUrl;
    public int version;
    public String hideOnError;

	public List<Item> items; // 活动集合
    public String type;
    public String group;
    public List<Section> sections;

    public int way;
    public String id;
    public Map<String, String>  trackParam;
    public String preload;
	public String deleteId;
	
	public Boolean valid;
//    public int version;
//    public String hideOnError;
	// private transient Properties trackParamProps;
    
//    private transient Template putiTemplate;
//	public String template; // 显示样式
//	public String templateUrl; // 动态模板布局文件

}
