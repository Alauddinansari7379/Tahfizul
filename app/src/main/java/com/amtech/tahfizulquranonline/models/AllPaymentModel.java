package com.amtech.tahfizulquranonline.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllPaymentModel{

//
//	String tranIdTxt , trandateTxt , payTypeTxt , amountTxt;
//
//	public String getTranIdTxt() {
//		return tranIdTxt;
//	}
//
//	public void setTranIdTxt(String tranIdTxt) {
//		this.tranIdTxt = tranIdTxt;
//	}
//
//	public String getTrandateTxt() {
//		return trandateTxt;
//	}
//
//	public void setTrandateTxt(String trandateTxt) {
//		this.trandateTxt = trandateTxt;
//	}
//
//	public String getPayTypeTxt() {
//		return payTypeTxt;
//	}
//
//	public void setPayTypeTxt(String payTypeTxt) {
//		this.payTypeTxt = payTypeTxt;
//	}
//
//	public String getAmountTxt() {
//		return amountTxt;
//	}
//
//	public void setAmountTxt(String amountTxt) {
//		this.amountTxt = amountTxt;
//	}
//
//	public AllPaymentModel(String tranIdTxt, String trandateTxt, String payTypeTxt, String amountTxt) {
//		this.tranIdTxt = tranIdTxt;
//		this.trandateTxt = trandateTxt;
//		this.payTypeTxt = payTypeTxt;
//		this.amountTxt = amountTxt;
//	}
	@SerializedName("data")
	private Data data;

	@SerializedName("status")
	private boolean status;

	public Data getData(){
		return data;
	}

	public boolean isStatus(){
		return status;
	}

	public List<AllPaymentModel> getAllData(){
		return (List<AllPaymentModel>) data;
	}
}