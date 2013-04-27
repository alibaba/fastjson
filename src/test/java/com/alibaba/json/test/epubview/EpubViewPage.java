package com.alibaba.json.test.epubview;

import java.io.Serializable;
import java.util.List;

public class EpubViewPage implements Serializable {

	private static final long serialVersionUID = -2198407838110786606L;
	
	//普通Epub书时使用
	private String src;
	private String imageUrl;
	private String pageNum;
	private List<EpubViewHotPoint> hotPoints;
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getPageNum() {
		return pageNum;
	}
	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}
	public List<EpubViewHotPoint> getHotPoints() {
		return hotPoints;
	}
	public void setHotPoints(List<EpubViewHotPoint> hotPoints) {
		this.hotPoints = hotPoints;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    public String getSrc()
    {
        return src;
    }
    public void setSrc(String src)
    {
        this.src = src;
    }
	
}
