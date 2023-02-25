package com.amtech.tahfizulquranonline.v2.countryCode;

import com.google.gson.annotations.SerializedName;

public class CountryCodeModel {

	@SerializedName("numcode")
	private int numcode;

	@SerializedName("iso")
	private String iso;

	@SerializedName("name")
	private String name;

	@SerializedName("nicename")
	private String nicename;

	@SerializedName("phonecode")
	private int phonecode;

	@SerializedName("id")
	private int id;

	@SerializedName("iso3")
	private String iso3;

	public int getNumcode(){
		return numcode;
	}

	public String getIso(){
		return iso;
	}

	public String getName(){
		return name;
	}

	public String getNicename(){
		return nicename;
	}

	public int getPhonecode(){
		return phonecode;
	}

	public int getId(){
		return id;
	}

	public String getIso3(){
		return iso3;
	}
}