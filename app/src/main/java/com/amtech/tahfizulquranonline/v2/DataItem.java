package com.amtech.tahfizulquranonline.v2;

import com.google.gson.annotations.SerializedName;

public class DataItem{

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
	private Object appDate;

	@SerializedName("pay_type")
	private String payType;

	@SerializedName("id")
	private int id;

	@SerializedName("inv_id")
	private String invId;

	@SerializedName("status")
	private int status;

	public String getEndDate(){
		return endDate;
	}

	public String getAmount(){
		return amount;
	}

	public String getUpdatedBy(){
		return updatedBy;
	}

	public String getSubId(){
		return subId;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getTxId(){
		return txId;
	}

	public String getTrxDate(){
		return trxDate;
	}

	public String getPayStatus(){
		return payStatus;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getUserId(){
		return userId;
	}

	public String getCreatedBy(){
		return createdBy;
	}

	public Object getAppDate(){
		return appDate;
	}

	public String getPayType(){
		return payType;
	}

	public int getId(){
		return id;
	}

	public String getInvId(){
		return invId;
	}

	public int getStatus(){
		return status;
	}
}