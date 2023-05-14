package com.alibaba.json.test.epubview;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 热点
 * @author renci
 *
 */
public class EpubViewHotPoint implements Serializable  {

	private static final long serialVersionUID = 2430184364840193603L;
	
	/**
	 * 热点类型：
	 * Normal：普通热点，显示图标，有iconzone、iconsrc标签
	 * Hide：隐藏热点，不会显示图标，无iconzone、iconsrc标签
	 * BkHightLight：背景高亮热点
	 */
	private String type;
	/**
	 * 热点行为名称
	 */
	private String actionname;
	
	/**
	 * 热点文件类型：
	 * text/plain：文本
	 * audio/mpeg：音频
	 * video/mpeg：视频
	 * image/jpeg：图片
	 * application/x-book：课文热点弹出框
	 * application/x-practice：课文练习
	 * office：office文档格式
	 * audio/highlight：音频背景高亮
	 * webview/swf：flash
	 */
	private String actiontype;
	
	/**
	 * 热点文件位置
	 */
	private String src;
	
	/**
	 * 加密热点文件位置
	 */
	private String dcfSrc;
	
	/**
	 * 默认热点文件原文件名
	 */
	private String description;
	
	/**
	 * application/x-book：课文热点弹出框（自定义热点）的标题
	 */
	private String title;
	
	/**
	 * application/x-book：课文热点弹出框（自定义热点）的内容
	 */
	private String content;
	
	/**
	 * application/x-book：课文热点弹出框（自定义热点）的附加菜单列表
	 */
	private String menulist;
	
	/**
	 * application/x-book：课文热点弹出框（自定义热点）的背景朗读音频源路径.
	 */
	private String reading;
	
	/**
	 * application/x-book：课文热点弹出框（自定义热点）是否支持画笔
	 */
	private String pen;
	
	/**
	 * application/x-book：课文热点弹出框（自定义热点）是否支持字典
	 */
	private String dictionary;
	
	/**
	 * 参数
	 */
	private Map<String, String> parameters;
	
	/**
	 * 图片区域左上角X值
	 */
	private String left;
	
	/**
	 * 图片区域左上角Y值
	 */
	private String top;
	
	/**
	 * 图片区域右下角X值
	 */
	private String right;
	
	/**
	 * 图片区域右下角Y值
	 */
	private String bottom;
	
	/**
	 * 热点图标存放路径，隐藏热点、背景高亮热点可以不含该值
	 */
	private String iconSrc;
	
	private String starttime;
	private String endtime;
	
	/**
	 * 热点区域
	 */
	private List<EpubViewHotPointZone> zoneList;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getActionname() {
		return actionname;
	}

	public void setActionname(String actionname) {
		this.actionname = actionname;
	}

	public String getActiontype() {
		return actiontype;
	}

	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getDcfSrc() {
		return dcfSrc;
	}

	public void setDcfSrc(String dcfSrc) {
		this.dcfSrc = dcfSrc;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMenulist() {
		return menulist;
	}

	public void setMenulist(String menulist) {
		this.menulist = menulist;
	}

	public String getReading() {
		return reading;
	}

	public void setReading(String reading) {
		this.reading = reading;
	}

	public String getPen() {
		return pen;
	}

	public void setPen(String pen) {
		this.pen = pen;
	}

	public String getDictionary() {
		return dictionary;
	}

	public void setDictionary(String dictionary) {
		this.dictionary = dictionary;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getLeft() {
		return left;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		this.right = right;
	}

	public String getBottom() {
		return bottom;
	}

	public void setBottom(String bottom) {
		this.bottom = bottom;
	}

	public String getIconSrc() {
		return iconSrc;
	}

	public void setIconSrc(String iconSrc) {
		this.iconSrc = iconSrc;
	}

	public List<EpubViewHotPointZone> getZoneList() {
		return zoneList;
	}

	public void setZoneList(List<EpubViewHotPointZone> zoneList) {
		this.zoneList = zoneList;
	}

    public String getStarttime()
    {
        return starttime;
    }

    public void setStarttime(String starttime)
    {
        this.starttime = starttime;
    }

    public String getEndtime()
    {
        return endtime;
    }

    public void setEndtime(String endtime)
    {
        this.endtime = endtime;
    }

    
}
