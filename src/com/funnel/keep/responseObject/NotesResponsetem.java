package com.funnel.keep.responseObject;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class NotesResponsetem {

	@JsonProperty("NoteTitle")
	public String NoteTitle;

	@JsonProperty("NoteText")
	public String NoteText;

	@JsonProperty("NoteBackgroundColor")
	public int NoteBackgroundColor;

	@JsonProperty("NoteCreationDate")
	public long NoteCreationDate;

	@JsonProperty("NoteType")
	public int NoteType;

	@JsonProperty("ChecklistItemsResponseItems")
	public List<ChecklistItemsResponseItem> ChecklistItemsResponseItems;

}
