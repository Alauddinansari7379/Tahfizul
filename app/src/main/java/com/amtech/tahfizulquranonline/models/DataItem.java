package com.amtech.tahfizulquranonline.models;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("course_id")
	private int courseId;

	@SerializedName("pay_status")
	private String payStatus;

	@SerializedName("talib_ilm_type")
	private int talibIlmType;

	@SerializedName("address")
	private String address;

	@SerializedName("madarsa_id")
	private int madarsaId;

	@SerializedName("name")
	private String name;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("state_id")
	private int stateId;

	@SerializedName("email")
	private String email;

	@SerializedName("country_id")
	private int countryId;

	@SerializedName("city_id")
	private int cityId;

	public void setCourseId(int courseId){
		this.courseId = courseId;
	}

	public int getCourseId(){
		return courseId;
	}

	public void setPayStatus(String payStatus){
		this.payStatus = payStatus;
	}

	public String getPayStatus(){
		return payStatus;
	}

	public void setTalibIlmType(int talibIlmType){
		this.talibIlmType = talibIlmType;
	}

	public int getTalibIlmType(){
		return talibIlmType;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setMadarsaId(int madarsaId){
		this.madarsaId = madarsaId;
	}

	public int getMadarsaId(){
		return madarsaId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return mobile;
	}

	public void setStateId(int stateId){
		this.stateId = stateId;
	}

	public int getStateId(){
		return stateId;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setCountryId(int countryId){
		this.countryId = countryId;
	}

	public int getCountryId(){
		return countryId;
	}

	public void setCityId(int cityId){
		this.cityId = cityId;
	}

	public int getCityId(){
		return cityId;
	}
}