package com.amtech.tahfizulquranonline.V2Data.courseModel;

import com.google.gson.annotations.SerializedName;

public class CourseItem {

	@SerializedName("updatedBy")
	private String updatedBy;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("createdBy")
	private String createdBy;

	@SerializedName("IsActive")
	private int isActive;

	@SerializedName("name")
	private String name;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	public String getUpdatedBy(){
		return updatedBy;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getCreatedBy(){
		return createdBy;
	}

	public int getIsActive(){
		return isActive;
	}

	public String getName(){
		return name;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getId(){
		return id;
	}
}