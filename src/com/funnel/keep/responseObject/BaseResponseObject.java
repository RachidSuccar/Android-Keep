package com.funnel.keep.responseObject;

import org.codehaus.jackson.annotate.JsonProperty;

public class BaseResponseObject {

	@JsonProperty("RequestStatus")
	public int RequestStatus;

	@JsonProperty("ErrorTitle")
	public String ErrorTitle;

	@JsonProperty("ErrorDescription")
	public String ErrorDescription;

	@JsonProperty("ExtraString")
	public String ExtraString;

}
