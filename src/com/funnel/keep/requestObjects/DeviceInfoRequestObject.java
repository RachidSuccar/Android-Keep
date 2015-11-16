package com.funnel.keep.requestObjects;

import org.codehaus.jackson.annotate.JsonProperty;

public class DeviceInfoRequestObject extends BaseRequestObject {

	@JsonProperty("OsVersion")
	public String OsVersion;

	@JsonProperty("DeviceName")
	public String DeviceName;

	@JsonProperty("DeviceUniqueID")
	public String DeviceUniqueID;

	@JsonProperty("DeviceNotificationToken")
	public String DeviceNotificationToken;

}
