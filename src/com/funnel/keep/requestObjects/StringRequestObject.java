package com.funnel.keep.requestObjects;

import org.codehaus.jackson.annotate.JsonProperty;

public class StringRequestObject extends BaseRequestObject {

	@JsonProperty("Text")
	public String Text;

}
