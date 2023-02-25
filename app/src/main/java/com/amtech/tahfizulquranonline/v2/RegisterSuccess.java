package com.amtech.tahfizulquranonline.v2;

import com.google.gson.annotations.SerializedName;

public class RegisterSuccess{

	@SerializedName("user_id")
	private int userId;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private boolean status;

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}
}