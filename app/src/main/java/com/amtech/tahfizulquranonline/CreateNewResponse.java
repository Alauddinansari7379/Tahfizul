package com.amtech.tahfizulquranonline;

import com.google.gson.annotations.SerializedName;

public class CreateNewResponse{

	@SerializedName("inv_id")
	private String invId;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private boolean status;

	public String getInvId(){
		return invId;
	}

	public String getMessage(){
		return message;
	}

	public boolean isStatus(){
		return status;
	}
}