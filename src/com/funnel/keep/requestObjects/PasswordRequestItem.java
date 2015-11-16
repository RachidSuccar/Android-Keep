package com.funnel.keep.requestObjects;

import org.codehaus.jackson.annotate.JsonProperty;

public class PasswordRequestItem {

	@JsonProperty("Title")
	public String Title;

	@JsonProperty("Account")
	public String Account;

	@JsonProperty("Text")
	public String Text;

	@JsonProperty("UserName")
	public String UserName;

	@JsonProperty("Description")
	public String Description;

	@JsonProperty("Color")
	public int Color;

}
