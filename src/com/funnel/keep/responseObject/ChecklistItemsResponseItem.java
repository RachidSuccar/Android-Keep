package com.funnel.keep.responseObject;

import org.codehaus.jackson.annotate.JsonProperty;

public class ChecklistItemsResponseItem {

	@JsonProperty("ChecklistItemTitle")
	public String ChecklistItemTitle;

	@JsonProperty("ChecklistItemStatus")
	public int ChecklistItemStatus;

}
