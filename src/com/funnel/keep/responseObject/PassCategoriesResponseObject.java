package com.funnel.keep.responseObject;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class PassCategoriesResponseObject  {

	@JsonProperty("CategName")
	public String CategName;

	@JsonProperty("Passwords")
	public List<PasswordResponseItem> Passwords;

}
