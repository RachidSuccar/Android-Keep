package com.funnel.keep.responseObject;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class RestoreResponseObject extends BaseResponseObject {


	@JsonProperty("PassCategories")
	public List<PassCategoriesResponseObject> PassCategories;

	@JsonProperty("NoteCategories")
	public List<NoteCategoriesResponseObject> NoteCategories;

}
