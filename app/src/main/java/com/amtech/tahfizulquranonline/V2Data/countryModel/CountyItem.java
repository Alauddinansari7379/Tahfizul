package com.amtech.tahfizulquranonline.V2Data.countryModel;

import com.google.gson.annotations.SerializedName;

public class CountyItem {

	@SerializedName("updatedBy")
	private String updatedBy;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("c_name")
	private String c_name;

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

	public CountyItem(String c_name, int countryId) {
		this.c_name = c_name;
		this.countryId = countryId;
	}

	public String getUpdatedBy(){
		return updatedBy;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getC_name() {
		return c_name;
	}

	public void setC_name(String c_name) {
		this.c_name = c_name;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
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