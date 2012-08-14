package cn.com.tx.domain.pagination;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pagination<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5038839734218582220L;

	private int totalResult = 0;

	private int totalPage = 1;

	private int pageIndex = 1;

	private int maxLength = 5;

	private int fromIndex = 0;

	private int toIndex = 0;

	private List<T> list = new ArrayList<T>();
	
	public Pagination(){
		
	}
	
	

	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}



	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}



	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}



	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}



	public void setFromIndex(int fromIndex) {
		this.fromIndex = fromIndex;
	}



	public void setToIndex(int toIndex) {
		this.toIndex = toIndex;
	}



	public int getFromIndex() {
		return this.fromIndex;
	}

	public int getToIndex() {
		return this.toIndex;
	}

	public int getNextPage() {
		if (this.pageIndex < this.totalPage) {
			return this.pageIndex + 1;
		} else {
			return this.pageIndex;
		}
	}

	public int getPrevPage() {
		if (this.pageIndex > 1) {
			return this.pageIndex - 1;
		}
		return this.pageIndex;
	}

	/**
	 * @return the currentPage
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * @return the list
	 */
	public List<T> getList() {
		if (list == null) {
			return new ArrayList<T>();
		}
		return new ArrayList<T>(list);
	}

	/**
	 * @return the totalPage
	 */
	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * @return the totalRecord
	 */
	public int getTotalResult() {
		return totalResult;
	}

	public int getMaxLength() {
		return maxLength;
	}

	/**
	 * 
	 * @param totalResult
	 * @param pageIndex
	 * @param maxLength
	 */
	public Pagination(int totalResult, int pageIndex, int maxLength) {
		if (maxLength > 0) {
			this.maxLength = maxLength;
		}
		if (totalResult > 0) {
			this.totalResult = totalResult;
		}
		if (pageIndex > 0) {
			this.pageIndex = pageIndex;
		}
		this.totalPage = this.totalResult / this.maxLength;
		if (this.totalResult % this.maxLength != 0) {
			this.totalPage = this.totalPage + 1;
		}
		if (this.totalPage == 0) {
			this.totalPage = 1;
		}
		if (this.pageIndex > this.totalPage) {
			this.pageIndex = this.totalPage;
		}
		if (this.pageIndex <= 0) {
			this.pageIndex = 1;
		}
		this.fromIndex = (this.pageIndex - 1) * maxLength;
		this.toIndex = this.fromIndex + maxLength;
		if (this.toIndex < 0) {
			this.toIndex = fromIndex;
		}
		if (this.toIndex > this.totalResult) {
			this.toIndex = this.totalResult;
		}

	}

	/**
	 * @param datas
	 *            the datas to set
	 */
	public void setList(List<T> list) {
		this.list = list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Pagination:\r\n");
		sb.append("\tpageIndex\t" + this.pageIndex + "\r\n");
		sb.append("\ttotalPage\t" + this.totalPage + "\r\n");
		sb.append("\tmaxLength\t" + this.maxLength + "\r\n");
		sb.append("\ttotalResult\t" + this.totalResult + "\r\n");
		for (T t : list) {
			sb.append(t + "\r\n");
		}
		return sb.toString();
	}
	
}
