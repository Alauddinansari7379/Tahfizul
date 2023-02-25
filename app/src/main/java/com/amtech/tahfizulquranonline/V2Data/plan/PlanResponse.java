package com.amtech.tahfizulquranonline.V2Data.plan;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class PlanResponse{

	@SerializedName("data")
	private List<PlanItem> data;

	@SerializedName("status")
	private boolean status;

	public void setData(List<PlanItem> data){
		this.data = data;
	}

	public List<PlanItem> getData(){
		return data;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}
}