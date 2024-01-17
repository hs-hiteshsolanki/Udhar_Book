package com.ub.udharbook.ModelResponse;

import com.google.gson.annotations.SerializedName;

public class Data{
	@SerializedName("business_name")
	private String businessName;
	@SerializedName("image")
	private String image;
	@SerializedName("phone")
	private String phone;
	@SerializedName("name")
	private String name;
	@SerializedName("location")
	private String location;

	public void setBusinessName(String businessName){
		this.businessName = businessName;
	}

	public String getBusinessName(){
		return businessName;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setLocation(String location){
		this.location = location;
	}

	public String getLocation(){
		return location;
	}
}
