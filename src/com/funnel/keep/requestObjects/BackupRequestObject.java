package com.funnel.keep.requestObjects;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class BackupRequestObject extends BaseRequestObject {

	@JsonProperty("Email")
	public String Email;

	@JsonProperty("HashedPassword")
	public String HashedPassword;

	@JsonProperty("PassCategories")
	public List<PassCategoryRequestItem> PassCategories;

	@JsonProperty("NoteCategories")
	public List<NoteCategoryRequestItem> NoteCategories;

	@JsonProperty("OsVersion")
	public String OsVersion;

	@JsonProperty("UniqueIdentifier")
	public String UniqueIdentifier;

	@JsonProperty("DeviceName")
	public String DeviceName;

}
