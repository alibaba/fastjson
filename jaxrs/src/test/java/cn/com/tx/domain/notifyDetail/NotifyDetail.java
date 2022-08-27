package cn.com.tx.domain.notifyDetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotifyDetail implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8760630447394929224L;

	private int detailId;

	private int hotId;

	private int templateId;

	private int srcId;

	private int destId;

	private boolean display;

	private Date foundTime;
	
	private List<String> args = new ArrayList<String>(); 
	

	public int getDetailId() {
		return detailId;
	}

	public void setDetailId(int detailId) {
		this.detailId = detailId;
	}

	public int getHotId() {
		return hotId;
	}

	public void setHotId(int hotId) {
		this.hotId = hotId;
	}

	public int getTemplateId() {
		return templateId;
	}

	public List<String> getArgs() {
		return args;
	}

	public void setArgs(List<String> args) {
		this.args = args;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public int getSrcId() {
		return srcId;
	}

	public void setSrcId(int srcId) {
		this.srcId = srcId;
	}

	public int getDestId() {
		return destId;
	}

	public void setDestId(int destId) {
		this.destId = destId;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public Date getFoundTime() {
		return foundTime;
	}

	public void setFoundTime(Date foundTime) {
		this.foundTime = foundTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hasCode = 0;
		if (this.detailId != 0) {
			hasCode += this.detailId;
		}
		return hasCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof NotifyDetail)) {
			return false;
		}
		return this.hashCode() == obj.hashCode();
	}
	
	
}
