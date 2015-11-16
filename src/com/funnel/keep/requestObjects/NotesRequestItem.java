package com.funnel.keep.requestObjects;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class NotesRequestItem {

	public int noteID;
	
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

	@JsonProperty("ChecklistItemsRequestItem")
	public List<ChecklistItemsRequestItem> ChecklistItemsRequestItem;

}
