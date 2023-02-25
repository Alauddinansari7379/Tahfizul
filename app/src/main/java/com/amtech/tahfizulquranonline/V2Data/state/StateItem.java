package com.amtech.tahfizulquranonline.V2Data.state;

import com.google.gson.annotations.SerializedName;

public class StateItem {

	@SerializedName("updatedBy")
	private String updatedBy;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("state_name")
	private String stateName;

	@SerializedName("createdBy")
	private String createdBy;

	@SerializedName("IsActive")
	private int isActive;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("country_id")
	private int countryId;

	public String getUpdatedBy(){
		return updatedBy;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getStateName(){
		return stateName;
	}

	public String getCreatedBy(){
		return createdBy;
	}

	public int getIsActive(){
		return isActive;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getId(){
		return id;
	}

	public int getCountryId(){
		return countryId;
	}
}