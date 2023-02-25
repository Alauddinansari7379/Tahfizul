package com.amtech.tahfizulquranonline.V2Data.state;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class StateResponse{

	@SerializedName("data")
	private List<StateItem> data;

	@SerializedName("status")
	private boolean status;

	public List<StateItem> getData(){
		return data;
	}

	public boolean isStatus(){
		return status;
	}
}