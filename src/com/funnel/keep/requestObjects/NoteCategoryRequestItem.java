package com.funnel.keep.requestObjects;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class NoteCategoryRequestItem {

	public int categoryID;
	
	@JsonProperty("NoteCategoryName")
	public String NoteCategoryName;

	@JsonProperty("Notes")
	public List<NotesRequestItem> Notes;

}
