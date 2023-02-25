package com.amtech.tahfizulquranonline.v2.moulimProfile;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GetMoulimProfileResponse{

	@SerializedName("data")
	private List<MProfileItem> data;

	@SerializedName("status")
	private boolean status;

	public void setData(List<MProfileItem> data){
		this.data = data;
	}

	public List<MProfileItem> getData(){
		return data;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}
}