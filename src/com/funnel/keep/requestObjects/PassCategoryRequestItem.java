package com.funnel.keep.requestObjects;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class PassCategoryRequestItem {

	public int cateID;

	@JsonProperty("CategName")
	public String CategName;

	@JsonProperty("Passwords")
	public List<PasswordRequestItem> Passwords;

}
