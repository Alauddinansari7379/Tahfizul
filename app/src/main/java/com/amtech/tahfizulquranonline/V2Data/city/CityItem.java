package com.amtech.tahfizulquranonline.V2Data.city;

import com.google.gson.annotations.SerializedName;

public class CityItem {

	@SerializedName("city_name")
	private String cityName;

	@SerializedName("updatedBy")
	private String updatedBy;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("createdBy")
	private String createdBy;

	@SerializedName("IsActive")
	private int isActive;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("state_id")
	private int stateId;

	@SerializedName("country_id")
	private int countryId;

	public void setCityName(String cityName){
		this.cityName = cityName;
	}

	public String getCityName(){
		return cityName;
	}

	public void setUpdatedBy(String updatedBy){
		this.updatedBy = updatedBy;
	}

	public String getUpdatedBy(){
		return updatedBy;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setCreatedBy(String createdBy){
		this.createdBy = createdBy;
	}

	public String getCreatedBy(){
		return createdBy;
	}

	public void setIsActive(int isActive){
		this.isActive = isActive;
	}

	public int getIsActive(){
		return isActive;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setStateId(int stateId){
		this.stateId = stateId;
	}

	public int getStateId(){
		return stateId;
	}

	public void setCountryId(int countryId){
		this.countryId = countryId;
	}

	public int getCountryId(){
		return countryId;
	}
}