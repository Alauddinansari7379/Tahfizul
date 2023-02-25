package com.amtech.tahfizulquranonline.v2;

import com.google.gson.annotations.SerializedName;

public class PaymentRespoItem {

	@SerializedName("end_date")
	private String endDate;

	@SerializedName("amount")
	private String amount;

	@SerializedName("updatedBy")
	private String updatedBy;

	@SerializedName("sub_id")
	private String subId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("tx_id")
	private String txId;

	@SerializedName("trx_date")
	private String trxDate;

	@SerializedName("pay_status")
	private String payStatus;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("createdBy")
	private String createdBy;

	@SerializedName("app_date")
	private String appDate;

	@SerializedName("pay_type")
	private String payType;

	@SerializedName("id")
	private int id;

	@SerializedName("inv_id")
	private String invId;

	@SerializedName("status")
	private int status;

	public void setEndDate(String endDate){
		this.endDate = endDate;
	}

	public String getEndDate(){
		return endDate;
	}

	public void setAmount(String amount){
		this.amount = amount;
	}

	public String getAmount(){
		return amount;
	}

	public void setUpdatedBy(String updatedBy){
		this.updatedBy = updatedBy;
	}

	public String getUpdatedBy(){
		return updatedBy;
	}

	public void setSubId(String subId){
		this.subId = subId;
	}

	public String getSubId(){
		return subId;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setTxId(String txId){
		this.txId = txId;
	}

	public String getTxId(){
		return txId;
	}

	public void setTrxDate(String trxDate){
		this.trxDate = trxDate;
	}

	public String getTrxDate(){
		return trxDate;
	}

	public void setPayStatus(String payStatus){
		this.payStatus = payStatus;
	}

	public String getPayStatus(){
		return payStatus;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setCreatedBy(String createdBy){
		this.createdBy = createdBy;
	}

	public String getCreatedBy(){
		return createdBy;
	}

	public void setAppDate(String appDate){
		this.appDate = appDate;
	}

	public String getAppDate(){
		return appDate;
	}

	public void setPayType(String payType){
		this.payType = payType;
	}

	public String getPayType(){
		return payType;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setInvId(String invId){
		this.invId = invId;
	}

	public String getInvId(){
		return invId;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}
}