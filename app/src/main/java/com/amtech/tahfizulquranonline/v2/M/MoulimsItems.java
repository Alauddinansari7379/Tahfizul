package com.amtech.tahfizulquranonline.v2.M;

import com.google.gson.annotations.SerializedName;

public class MoulimsItems {

	@SerializedName("image")
	private String image;

	@SerializedName("address")
	private String address;

	@SerializedName("updatedBy")
	private String updatedBy;

	@SerializedName("madarsa_id")
	private int madarsaId;

	@SerializedName("IsActive")
	private int isActive;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("about")
	private String about;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("password")
	private String password;

	@SerializedName("mualem_type_id")
	private int mualemTypeId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("createdBy")
	private String createdBy;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("state_id")
	private int stateId;

	@SerializedName("email")
	private String email;

	@SerializedName("country_id")
	private int countryId;

	@SerializedName("city_id")
	private int cityId;

	@SerializedName("experience")
	private String experience;

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setUpdatedBy(String updatedBy){
		this.updatedBy = updatedBy;
	}

	public String getUpdatedBy(){
		return updatedBy;
	}

	public void setMadarsaId(int madarsaId){
		this.madarsaId = madarsaId;
	}

	public int getMadarsaId(){
		return madarsaId;
	}

	public void setIsActive(int isActive){
		this.isActive = isActive;
	}

	public int getIsActive(){
		return isActive;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return mobile;
	}

	public void setAbout(String about){
		this.about = about;
	}

	public String getAbout(){
		return about;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setMualemTypeId(int mualemTypeId){
		this.mualemTypeId = mualemTypeId;
	}

	public int getMualemTypeId(){
		return mualemTypeId;
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

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
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