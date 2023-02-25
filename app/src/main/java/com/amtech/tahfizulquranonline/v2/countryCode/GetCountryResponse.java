package com.amtech.tahfizulquranonline.v2.countryCode;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GetCountryResponse{

	@SerializedName("data")
	private List<CountryCodeModel> data;

	@SerializedName("status")
	private boolean status;

	public List<CountryCodeModel> getData(){
		return data;
	}

	public boolean isStatus(){
		return status;
	}
}