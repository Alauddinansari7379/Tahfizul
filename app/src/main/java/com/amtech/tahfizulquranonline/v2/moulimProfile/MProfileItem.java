package com.amtech.tahfizulquranonline.v2.moulimProfile;

import com.google.gson.annotations.SerializedName;

public class MProfileItem {

	@SerializedName("course_id")
	private int courseId;

	@SerializedName("time_slot_id")
	private int timeSlotId;

	@SerializedName("strength_values")
	private String strengthValues;

	@SerializedName("updatedBy")
	private String updatedBy;

	@SerializedName("madarsa_id")
	private int madarsaId;

	@SerializedName("slot_type_id")
	private int slotTypeId;

	@SerializedName("IsActive")
	private int isActive;

	@SerializedName("end_time")
	private String endTime;

	@SerializedName("mualem_id")
	private int mualemId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("start_time")
	private String startTime;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("createdBy")
	private String createdBy;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	public void setCourseId(int courseId){
		this.courseId = courseId;
	}

	public int getCourseId(){
		return courseId;
	}

	public void setTimeSlotId(int timeSlotId){
		this.timeSlotId = timeSlotId;
	}

	public int getTimeSlotId(){
		return timeSlotId;
	}

	public void setStrengthValues(String strengthValues){
		this.strengthValues = strengthValues;
	}

	public String getStrengthValues(){
		return strengthValues;
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

	public void setSlotTypeId(int slotTypeId){
		this.slotTypeId = slotTypeId;
	}

	public int getSlotTypeId(){
		return slotTypeId;
	}

	public void setIsActive(int isActive){
		this.isActive = isActive;
	}

	public int getIsActive(){
		return isActive;
	}

	public void setEndTime(String endTime){
		this.endTime = endTime;
	}

	public String getEndTime(){
		return endTime;
	}

	public void setMualemId(int mualemId){
		this.mualemId = mualemId;
	}

	public int getMualemId(){
		return mualemId;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setStartTime(String startTime){
		this.startTime = startTime;
	}

	public String getStartTime(){
		return startTime;
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
}