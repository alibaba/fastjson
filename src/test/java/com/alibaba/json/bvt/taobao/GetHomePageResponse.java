package com.alibaba.json.bvt.taobao;

import com.alibaba.fastjson.annotation.JSONType;

//@JSONType(orders={"api", "v", "ret", "data"})
public class GetHomePageResponse {
    public String api;
    public String v;
    public String[] ret;

	public GetHomePageData data;

}
