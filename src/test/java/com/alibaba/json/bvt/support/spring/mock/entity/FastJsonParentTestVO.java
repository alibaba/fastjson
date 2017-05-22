/**
 * <p>Title: FastJsonParentTestVO.java</p>
 * <p>Description: FastJsonParentTestVO</p>
 * <p>Package: com.alibaba.json.bvt.support.spring.mock.entity</p>
 * <p>Company: www.github.com/DarkPhoenixs</p>
 * <p>Copyright: Dark Phoenixs (Open-Source Organization) 2016</p>
 */
package com.alibaba.json.bvt.support.spring.mock.entity;

import java.util.List;

/**
 * <p>Title: FastJsonParentTestVO</p>
 * <p>Description: </p>
 *
 * @since 2016年4月20日
 * @author Victor.Zxy
 * @version 1.0
 */
public class FastJsonParentTestVO {

	private String name;
	
	private List<FastJsonSonTestVO> sonList;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the sonList
	 */
	public List<FastJsonSonTestVO> getSonList() {
		return sonList;
	}

	/**
	 * @param sonList the sonList to set
	 */
	public void setSonList(List<FastJsonSonTestVO> sonList) {
		this.sonList = sonList;
	}
	
}
