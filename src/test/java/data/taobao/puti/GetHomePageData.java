package data.taobao.puti;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;

import data.taobao.puti.model.Section;

// section, interval, timestamp, WriteCacheTimestamp, digest, userId, way, 
// @JSONType(orders={"section", "interval", "timeStamp", "traceId", "digest", "way", "WriteCacheTimestamp", "userId"})
public class GetHomePageData  {

	//private static final long serialVersionUID = -684313709852285893L;

	public List<Section> section;

	public long interval;

	@JSONField(name="timeStamp")
	public String timestamp;

	public String digest;   //摘要信息

	public int way;//0：全量，1：增量


	public long WriteCacheTimestamp;
	
	public String userId;
	
	public String traceId;
}
