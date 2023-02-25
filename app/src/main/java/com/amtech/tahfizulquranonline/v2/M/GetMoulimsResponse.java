package com.amtech.tahfizulquranonline.v2.M;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GetMoulimsResponse{

	@SerializedName("data")
	private List<MoulimsItems> data;

	@SerializedName("status")
	private boolean status;

	public void setData(List<MoulimsItems> data){
		this.data = data;
	}

	public List<MoulimsItems> getData(){
		return data;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}
}