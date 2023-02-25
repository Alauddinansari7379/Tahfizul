package com.amtech.tahfizulquranonline.V2Data.countryModel;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryModel {

	@SerializedName("data")
	private List<CountyItem> data;

	@SerializedName("status")
	private boolean status;

	public List<CountyItem> getData(){
		return data;
	}

	public boolean isStatus(){
		return status;
	}

	public List<CountyItem> getCountry(){
		return data;
	}
}