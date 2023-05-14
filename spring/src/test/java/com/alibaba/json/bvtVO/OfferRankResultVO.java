package com.alibaba.json.bvtVO;

import java.util.ArrayList;
import java.util.List;

public class OfferRankResultVO {
	private List<SearchCenterOfferModel> models = new ArrayList<SearchCenterOfferModel>();
	
	public OfferRankResultVO() {
		models.add(new SearchCenterOfferModel());
	}

	public List<SearchCenterOfferModel> getModel() {
		return models;
	}

	public void setModel(List<SearchCenterOfferModel> models) {
		this.models = models;
	}

}

class SearchCenterOfferModel {
	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}