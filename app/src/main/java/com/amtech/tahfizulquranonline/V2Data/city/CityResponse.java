package com.amtech.tahfizulquranonline.V2Data.city;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CityResponse{

	@SerializedName("data")
	private List<CityItem> data;

	@SerializedName("status")
	private boolean status;

	public void setData(List<CityItem> data){
		this.data = data;
	}

	public List<CityItem> getData(){
		return data;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}
}