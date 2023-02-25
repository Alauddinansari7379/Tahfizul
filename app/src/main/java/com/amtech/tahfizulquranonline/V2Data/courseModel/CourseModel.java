package com.amtech.tahfizulquranonline.V2Data.courseModel;

import java.util.Collection;
import java.util.List;

import com.amtech.tahfizulquranonline.V2Data.countryModel.CountyItem;
import com.google.gson.annotations.SerializedName;

public class CourseModel{

	@SerializedName("data")
	private List<CourseItem> data;

	@SerializedName("status")
	private boolean status;

	public List<CourseItem> getData(){
		return data;
	}

	public List<CourseItem> getCourse(){
		return data;
	}

	public boolean isStatus(){
		return status;
	}
}