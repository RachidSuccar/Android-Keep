package com.funnel.keep.requestObjects;

import org.codehaus.jackson.annotate.JsonProperty;

public class ChecklistItemsRequestItem {

	
	@JsonProperty("ChecklistItemTitle")
	public String ChecklistItemTitle;

	@JsonProperty("ChecklistItemStatus")
	public int ChecklistItemStatus;

}
