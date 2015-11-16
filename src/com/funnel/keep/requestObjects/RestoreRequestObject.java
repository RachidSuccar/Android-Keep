package com.funnel.keep.requestObjects;

import org.codehaus.jackson.annotate.JsonProperty;

public class RestoreRequestObject extends BaseRequestObject {

	@JsonProperty("Email")
	public String Email;

	@JsonProperty("HashedPassword")
	public String HashedPassword;

}
