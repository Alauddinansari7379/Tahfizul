package com.amtech.tahfizulquranonline.V2Data.plan;

import com.google.gson.annotations.SerializedName;

public class PlanItem {

	@SerializedName("madarsha_id")
	private int madarshaId;

	@SerializedName("updatedBy")
	private String updatedBy;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("createdBy")
	private String createdBy;

	@SerializedName("price")
	private String price;

	@SerializedName("IsActive")
	private int isActive;

	@SerializedName("name")
	private String name;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("days")
	private String days;

	@SerializedName("description")
	private String description;

	@SerializedName("id")
	private int id;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setMadarshaId(int madarshaId){
		this.madarshaId = madarshaId;
	}

	public int getMadarshaId(){
		return madarshaId;
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

	public void setPrice(String price){
		this.price = price;
	}

	public String getPrice(){
		return price;
	}

	public void setIsActive(int isActive){
		this.isActive = isActive;
	}

	public int getIsActive(){
		return isActive;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setDays(String days){
		this.days = days;
	}

	public String getDays(){
		return days;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}