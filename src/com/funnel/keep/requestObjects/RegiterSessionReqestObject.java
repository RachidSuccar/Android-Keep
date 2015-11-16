package com.funnel.keep.requestObjects;

import org.codehaus.jackson.annotate.JsonProperty;

public class RegiterSessionReqestObject extends DeviceInfoRequestObject  {
	
	@JsonProperty("BrowserFingerprint")
	public String BrowserFingerprint;

}
