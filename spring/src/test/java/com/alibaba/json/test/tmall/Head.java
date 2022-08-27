/**
 * Taobao.com Inc.
 * Copyright (c) 2003-2012 All Rights Reserved.
 */
package com.alibaba.json.test.tmall;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;

/**
 * ͷ����Ϣ
 * @author benxiang.hhq
 * @version $Id: Head.java, v 0.1 2012-12-26 ����5:48:58 benxiang.hhq Exp $
 */
public class Head {
    @JSONField(name = "Status")
    private String status;
    @JSONField(name = "SearchTime")
    private String searchTime;
    @JSONField(name = "Version")
    private String version;
    @JSONField(name = "DocsFound")
    private String docsFound;
    @JSONField(name = "DocsRestrict")
    private String docsRestrict;
    @JSONField(name = "DocsReturn")
    private String docsReturn;
    @JSONField(name = "DocsSearch")
    private String docsSearch;
    @JSONField(name="HasPrePage")
    private String hasPrePage;
	@JSONField(name="HasNextPage")
    private String hasNextPage;
	public String getHasPrePage() {
		return hasPrePage;
	}
	public void setHasPrePage(String hasPrePage) {
		this.hasPrePage = hasPrePage;
	}
	public String getHasNextPage() {
		return hasNextPage;
	}
	public void setHasNextPage(String hasNextPage) {
		this.hasNextPage = hasNextPage;
	}
    public String getDocsFound() {
        return docsFound;
    }
    public void setDocsFound(String docsFound) {
        this.docsFound = docsFound;
    }
    public String getDocsRestrict() {
        return docsRestrict;
    }
    public void setDocsRestrict(String docsRestrict) {
        this.docsRestrict = docsRestrict;
    }
    public String getDocsReturn() {
        return docsReturn;
    }
    public void setDocsReturn(String docsReturn) {
        this.docsReturn = docsReturn;
    }
    public String getDocsSearch() {
        return docsSearch;
    }
    public void setDocsSearch(String docsSearch) {
        this.docsSearch = docsSearch;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getSearchTime() {
        return searchTime;
    }
    public void setSearchTime(String searchTime) {
        this.searchTime = searchTime;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
}