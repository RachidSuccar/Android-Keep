package com.funnel.keep.responseObject;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class NoteCategoriesResponseObject {

	@JsonProperty("CategName")
	public String CategName;

	@JsonProperty("Notes")
	public List<NotesResponsetem> Notes;

}
