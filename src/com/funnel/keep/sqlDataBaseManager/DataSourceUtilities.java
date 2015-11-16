package com.funnel.keep.sqlDataBaseManager;

import java.util.ArrayList;

import com.funnel.keep.requestObjects.ChecklistItemsRequestItem;
import com.funnel.keep.requestObjects.NoteCategoryRequestItem;
import com.funnel.keep.requestObjects.NotesRequestItem;
import com.funnel.keep.requestObjects.PassCategoryRequestItem;
import com.funnel.keep.requestObjects.PasswordRequestItem;
import com.funnel.keep.decorator.NoteCategoriesDataModelObject;
import com.funnel.keep.decorator.PassCategoriesDataModelObject;

public class DataSourceUtilities {

	public static int UNAVAILABLE_INDEX = -1;

	public static PassCategoryRequestItem setPassDetails(
			PassCategoriesDataModelObject dbItem,
			PassCategoryRequestItem newItem, boolean isDuplicated) {

		if (!isDuplicated) {

			newItem.cateID = dbItem.categoryID;
			newItem.CategName = dbItem.categoryTitle;

		}
		if (dbItem.passwordID != 0) {
			if (newItem.Passwords == null)
				newItem.Passwords = new ArrayList<PasswordRequestItem>();
			PasswordRequestItem pass = new PasswordRequestItem();
			pass.Account = dbItem.passAccount;
			pass.Color = dbItem.passBackgroundColor;
			pass.Description = dbItem.passDescription;
			pass.Text = dbItem.passText;
			pass.Title = dbItem.passTitle;
			pass.UserName = dbItem.passUserName;

			newItem.Passwords.add(pass);
		}

		return newItem;
	}

	public static NoteCategoryRequestItem setNoteDetail(
			NoteCategoryRequestItem item, NoteCategoriesDataModelObject dbItem,
			int categoryDuplicatedIndex, int notDuplicatedIndex) {

		if (categoryDuplicatedIndex == UNAVAILABLE_INDEX) {
			item.categoryID = dbItem.categoryID;
			item.NoteCategoryName = dbItem.categoryTitle;
		}

		addNoteToCate(dbItem, item, notDuplicatedIndex);

		addChecklistToNote(dbItem, item, notDuplicatedIndex);

		return item;
	}

	private static void addNoteToCate(NoteCategoriesDataModelObject dbItem,
			NoteCategoryRequestItem item, int categoryDuplicatedIndex) {

		if (dbItem.noteID != 0 && categoryDuplicatedIndex == UNAVAILABLE_INDEX) {
			if (item.Notes == null)
				item.Notes = new ArrayList<NotesRequestItem>();
			NotesRequestItem newNote = new NotesRequestItem();
			newNote.NoteType = dbItem.type;
			newNote.NoteTitle = dbItem.Title;
			newNote.NoteText = dbItem.Text;
			newNote.noteID = dbItem.noteID;
			newNote.NoteCreationDate = dbItem.creationDate;
			newNote.NoteBackgroundColor = dbItem.BackgroundColor;
			item.Notes.add(newNote);
		}

	}

	private static void addChecklistToNote(
			NoteCategoriesDataModelObject dbItem, NoteCategoryRequestItem item,
			int index) {
		if (dbItem.checklistID != 0) {
			ChecklistItemsRequestItem newChecklist = new ChecklistItemsRequestItem();
			newChecklist.ChecklistItemStatus = dbItem.checklistStatus;
			newChecklist.ChecklistItemTitle = dbItem.checklistText;
			if (index == UNAVAILABLE_INDEX) {
				if (item.Notes.get(item.Notes.size() - 1).ChecklistItemsRequestItem == null)
					item.Notes.get(item.Notes.size() - 1).ChecklistItemsRequestItem = new ArrayList<ChecklistItemsRequestItem>();
				item.Notes.get(item.Notes.size() - 1).ChecklistItemsRequestItem
						.add(newChecklist);

			} else {
				if (item.Notes.get(index).ChecklistItemsRequestItem == null)
					item.Notes.get(index).ChecklistItemsRequestItem = new ArrayList<ChecklistItemsRequestItem>();
				item.Notes.get(index).ChecklistItemsRequestItem
						.add(newChecklist);
			}

		}

	}
	
	

}
