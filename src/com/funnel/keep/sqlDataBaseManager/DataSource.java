package com.funnel.keep.sqlDataBaseManager;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import com.funnel.keep.requestObjects.ChecklistItemsRequestItem;
import com.funnel.keep.requestObjects.NoteCategoryRequestItem;
import com.funnel.keep.requestObjects.NotesRequestItem;
import com.funnel.keep.requestObjects.PassCategoryRequestItem;
import com.funnel.keep.requestObjects.PasswordRequestItem;
import com.funnel.keep.responseObject.RestoreResponseObject;
import com.funnel.keep.utilities.CommonUtilities;
import com.funnel.keep.utilities.Cryptography;
import com.funnel.keep.utilities.Utilities;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import com.funnel.keep.base.BaseItem;

import com.funnel.keep.R;

import com.funnel.keep.decorator.CategoryItems;
import com.funnel.keep.decorator.ChangeAppPassObject;
import com.funnel.keep.decorator.Constants;
import com.funnel.keep.decorator.DataBaseDecorator;
import com.funnel.keep.decorator.NoteCategoriesDataModelObject;
import com.funnel.keep.decorator.NoteItem;
import com.funnel.keep.decorator.NoteItem.ChecklistItem;
import com.funnel.keep.decorator.PassCategoriesDataModelObject;
import com.funnel.keep.decorator.PasswordItem;
import com.funnel.keep.decorator.SpecificNoteDataModelObject;

public class DataSource extends BaseDataSource {

	private SecretKeySpec secretKeySpec;

	public DataSource(Context context, SecretKeySpec _secretKeySpec) {

		if (dbHelper == null) {
			dbHelper = new SQLiteHelper(context);
		}
		this.secretKeySpec = _secretKeySpec;

	}

	public void setNewScretKey(SecretKeySpec newSecretKeySpec) {
		this.secretKeySpec = newSecretKeySpec;
	}

	public DataSource(Context context) {

		if (dbHelper == null) {
			dbHelper = new SQLiteHelper(context);
		}

	}

	public int getDbVersion() {

		return database.getVersion();
	}

	// TABLE SETTINGS
	public void setMainAppPassword(String password) {

		ContentValues values = new ContentValues();
		values.put(DataBaseDecorator.TABLE_SETTINGS[2], password);
		if (CheckIfTableisEmpty(DataBaseDecorator.TABLE_SETTINGS[0])) {

			database.insert(DataBaseDecorator.TABLE_SETTINGS[0], null, values);

		} else {
			database.update(DataBaseDecorator.TABLE_SETTINGS[0], values,
					DataBaseDecorator.TABLE_SETTINGS[1] + " = " + 1, null);
		}

	}

	public String getPassword() {
		String password = null;
		String query = "SELECT " + DataBaseDecorator.TABLE_SETTINGS[2]
				+ " FROM " + DataBaseDecorator.TABLE_SETTINGS[0];
		Cursor cursor = database.rawQuery(query, null);

		if (cursor.moveToFirst()) {
			password = cursor.getString(0);

		}
		cursor.close();
		return password;

	}

	public boolean CheckIfTableisEmpty(String tableName) {
		String query = "SELECT " + "_id" + " FROM " + tableName + " ORDER BY "
				+ "_id" + " DESC LIMIT 1";
		Cursor cur = database.rawQuery(query, null);

		boolean IsEmpty;
		if (cur != null && cur.getCount() > 0) {
			IsEmpty = false;

		} else {
			IsEmpty = true;
		}
		cur.close();
		return IsEmpty;

	}

	// PASSWORDS
	public int creatNewPassword(String passwordTitle, String passwordAccount,
			String passwordText, String passwordUserName,
			String passwordDescription, int passwordIconbg, long date,
			int categoryID) {
		passwordText = Cryptography.encrypt(passwordText, secretKeySpec);
		ContentValues values = new ContentValues();
		values.put(DataBaseDecorator.TABLE_PASSWORDS[2], passwordTitle);
		values.put(DataBaseDecorator.TABLE_PASSWORDS[3], passwordAccount);
		values.put(DataBaseDecorator.TABLE_PASSWORDS[4], passwordText);
		values.put(DataBaseDecorator.TABLE_PASSWORDS[5], passwordUserName);
		values.put(DataBaseDecorator.TABLE_PASSWORDS[6], passwordDescription);

		values.put(DataBaseDecorator.TABLE_PASSWORDS[7], passwordIconbg);
		values.put(DataBaseDecorator.TABLE_PASSWORDS[8], date);
		values.put(DataBaseDecorator.TABLE_PASSWORDS[9], categoryID);
		return (int) database.insert(DataBaseDecorator.TABLE_PASSWORDS[0],
				null, values);
	}

	public int creatNewPasswordWithoutEncry(String passwordTitle,
			String passwordAccount, String passwordText,
			String passwordUserName, String passwordDescription,
			int passwordIconbg, long date, int categoryID) {

		ContentValues values = new ContentValues();
		values.put(DataBaseDecorator.TABLE_PASSWORDS[2], passwordTitle);
		values.put(DataBaseDecorator.TABLE_PASSWORDS[3], passwordAccount);
		values.put(DataBaseDecorator.TABLE_PASSWORDS[4], passwordText);
		values.put(DataBaseDecorator.TABLE_PASSWORDS[5], passwordUserName);
		values.put(DataBaseDecorator.TABLE_PASSWORDS[6], passwordDescription);

		values.put(DataBaseDecorator.TABLE_PASSWORDS[7], passwordIconbg);
		values.put(DataBaseDecorator.TABLE_PASSWORDS[8], date);
		values.put(DataBaseDecorator.TABLE_PASSWORDS[9], categoryID);
		return (int) database.insert(DataBaseDecorator.TABLE_PASSWORDS[0],
				null, values);
	}

	public ArrayList<PasswordItem> gePasswordsForCategory(int categoryID) {
		ArrayList<PasswordItem> passwords = new ArrayList<PasswordItem>();
		Cursor cursor = database.rawQuery("select * from "
				+ DataBaseDecorator.TABLE_PASSWORDS[0] + " WHERE "
				+ DataBaseDecorator.TABLE_PASSWORDS[9] + " = " + categoryID
				+ " ORDER BY " + DataBaseDecorator.TABLE_PASSWORDS[2]
				+ " COLLATE NOCASE ASC ", null);
		if (cursor.moveToFirst()) {
			do {
				PasswordItem item = new PasswordItem();
				item.itemID = cursor.getInt(0);
				item.itemTitle = cursor.getString(1);
				item.passwordAccount = cursor.getString(2);
				item.itemText = Cryptography.decrypt(secretKeySpec,
						cursor.getString(3));
				item.passwordUsername = cursor.getString(4);
				item.passwordDescription = cursor.getString(5);
				item.itemBackgroundColor = cursor.getInt(6);
				item.itemCreationDate = cursor.getInt(7);
				item.itemCategoryID = cursor.getInt(8);
				passwords.add(item);

			} while (cursor.moveToNext());

			cursor.close();
		}
		return passwords;
	}

	public int deletePassword(int passordID) {
		return (int) database.delete(DataBaseDecorator.TABLE_PASSWORDS[0],
				DataBaseDecorator.TABLE_PASSWORDS[1] + " = ?",
				new String[] { String.valueOf(passordID) });
	}

	public int deleteNote(int noteID) {
		return (int) database.delete(DataBaseDecorator.TABLE_NOTES[0],
				DataBaseDecorator.TABLE_NOTES[1] + " = ?",
				new String[] { String.valueOf(noteID) });
	}

	public PasswordItem getPasswordDetails(int passwordID) {
		PasswordItem item = new PasswordItem();
		String query = "SELECT * FROM " + DataBaseDecorator.TABLE_PASSWORDS[0]
				+ " WHERE " + DataBaseDecorator.TABLE_PASSWORDS[1] + " = "
				+ passwordID;
		Cursor cursor = database.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			item.itemTitle = cursor.getString(1);
			item.passwordAccount = cursor.getString(2);
			item.itemText = Cryptography.decrypt(secretKeySpec,
					cursor.getString(3));
			item.passwordUsername = cursor.getString(4);
			item.passwordDescription = cursor.getString(5);
			item.itemBackgroundColor = cursor.getInt(6);
		}
		cursor.close();
		return item;

	}

	public int updatePassword(int passwordID, String passwordTitle,
			String passwordAccount, String passwordText,
			String passwordUserName, String passwordDescription,
			int passwordIconbg) {
		passwordText = Cryptography.encrypt(passwordText, secretKeySpec);
		ContentValues values = new ContentValues();
		values.put(DataBaseDecorator.TABLE_PASSWORDS[2], passwordTitle);
		values.put(DataBaseDecorator.TABLE_PASSWORDS[3], passwordAccount);
		values.put(DataBaseDecorator.TABLE_PASSWORDS[4], passwordText);
		values.put(DataBaseDecorator.TABLE_PASSWORDS[5], passwordUserName);
		values.put(DataBaseDecorator.TABLE_PASSWORDS[6], passwordDescription);

		values.put(DataBaseDecorator.TABLE_PASSWORDS[7], passwordIconbg);
		return (int) database.update(DataBaseDecorator.TABLE_PASSWORDS[0],
				values, DataBaseDecorator.TABLE_PASSWORDS[1] + " = "
						+ passwordID, null);
	}

	public int deletePasswordsForCategoryID(int categoryID) {
		List<String> passwordsIDs = new ArrayList<String>();
		Cursor cursor = database.rawQuery("select * from "
				+ DataBaseDecorator.TABLE_PASSWORDS[0] + " WHERE "
				+ DataBaseDecorator.TABLE_PASSWORDS[9] + " = " + categoryID
				+ " order by " + DataBaseDecorator.TABLE_PASSWORDS[1]
				+ " desc ", null);
		int size = cursor.getCount();

		if (cursor.moveToFirst()) {
			do {
				int passwordsID;
				passwordsID = cursor.getInt(0);
				passwordsIDs.add(String.valueOf(passwordsID));

			} while (cursor.moveToNext());

			cursor.close();
		}
		String args = TextUtils.join(", ", passwordsIDs);
		if (size == 0) {
			return 9;

		} else {
			return (int) database
					.delete(DataBaseDecorator.TABLE_PASSWORDS[0],
							DataBaseDecorator.TABLE_PASSWORDS[1] + " IN ("
									+ args + ")", null);
		}

	}

	public int changePasswordsCategoryName(int categoryID,
			String newCategoryName) {

		ContentValues value = new ContentValues();
		value.put(DataBaseDecorator.TABLE_PASSWORDS_CATEGORIES[2],
				newCategoryName);
		return (int) database.update(
				DataBaseDecorator.TABLE_PASSWORDS_CATEGORIES[0], value,
				DataBaseDecorator.TABLE_PASSWORDS_CATEGORIES[1] + " = "
						+ categoryID, null);

	}

	public int changeNoteCategoryName(int categoryID, String newCategoryName) {

		ContentValues value = new ContentValues();
		value.put(DataBaseDecorator.TABLE_NOTES_CATEGORIES[2], newCategoryName);
		return (int) database.update(
				DataBaseDecorator.TABLE_NOTES_CATEGORIES[0], value,
				DataBaseDecorator.TABLE_NOTES_CATEGORIES[1] + " = "
						+ categoryID, null);

	}

	public int createNewPasswordsCategory(String title) {

		ContentValues values = new ContentValues();
		values.put(DataBaseDecorator.TABLE_PASSWORDS_CATEGORIES[2], title);
		return (int) database.insert(
				DataBaseDecorator.TABLE_PASSWORDS_CATEGORIES[0], null, values);

	}

	public boolean deletePasswordsCategory(int categoryId) {

		if (database.delete(DataBaseDecorator.TABLE_PASSWORDS_CATEGORIES[0],
				DataBaseDecorator.TABLE_PASSWORDS_CATEGORIES[1] + " = ?",
				new String[] { String.valueOf(categoryId) }) != 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean deleteNoteCategory(int categoryId) {

		if (database.delete(DataBaseDecorator.TABLE_NOTES_CATEGORIES[0],
				DataBaseDecorator.TABLE_NOTES_CATEGORIES[1] + " = ?",
				new String[] { String.valueOf(categoryId) }) != 0) {
			return true;
		} else {
			return false;
		}

	}

	public void deleteUserInfos() {
		database.execSQL("delete from "
				+ DataBaseDecorator.TABLE_PASSWORDS_CATEGORIES[0]);
		database.execSQL("delete from "
				+ DataBaseDecorator.TABLE_NOTES_CATEGORIES[0]);

	}

	public void restoreUserInfos(RestoreResponseObject restoreResponseObject,
			SecretKeySpec secretKeySpec) {

		database.beginTransaction();

		try {
			// delete user old pass and notes
			deleteUserInfos();

			// inserting new pass for user

			if (restoreResponseObject.PassCategories != null
					&& !restoreResponseObject.PassCategories.isEmpty()) {
				for (int i = 0; i < restoreResponseObject.PassCategories.size(); i++) {

					int tempID = createNewPasswordsCategory(restoreResponseObject.PassCategories
							.get(i).CategName);
					if (restoreResponseObject.PassCategories.get(i).Passwords != null
							&& !restoreResponseObject.PassCategories.get(i).Passwords
									.isEmpty() && tempID > 0) {
						for (int u = 0; u < restoreResponseObject.PassCategories
								.get(i).Passwords.size(); u++) {
							creatNewPassword(
									restoreResponseObject.PassCategories.get(i).Passwords
											.get(u).Title,
									restoreResponseObject.PassCategories.get(i).Passwords
											.get(u).Account,
									Cryptography
											.decrypt(
													secretKeySpec,
													restoreResponseObject.PassCategories
															.get(i).Passwords
															.get(u).Text),
									restoreResponseObject.PassCategories.get(i).Passwords
											.get(u).UserName,
									restoreResponseObject.PassCategories.get(i).Passwords
											.get(u).Description,
									restoreResponseObject.PassCategories.get(i).Passwords
											.get(u).Color, CommonUtilities
											.timeInSecond(), tempID);
						}
					}

				}

			}
			if (restoreResponseObject.NoteCategories != null
					&& !restoreResponseObject.NoteCategories.isEmpty()) {
				for (int j = 0; j < restoreResponseObject.NoteCategories.size(); j++) {
					int tempNoteCategory = createNewNoteCategory(restoreResponseObject.NoteCategories
							.get(j).CategName);
					if (restoreResponseObject.NoteCategories.get(j).Notes != null
							&& !restoreResponseObject.NoteCategories.get(j).Notes
									.isEmpty() && tempNoteCategory > 0) {
						for (int noteIndex = 0; noteIndex < restoreResponseObject.NoteCategories
								.get(j).Notes.size(); noteIndex++) {
							int tempNoteID = creatNewNoteWithoutEncry(
									restoreResponseObject.NoteCategories.get(j).Notes
											.get(noteIndex).NoteTitle,
									restoreResponseObject.NoteCategories.get(j).Notes
											.get(noteIndex).NoteText,
									restoreResponseObject.NoteCategories.get(j).Notes
											.get(noteIndex).NoteText,
									restoreResponseObject.NoteCategories.get(j).Notes
											.get(noteIndex).NoteBackgroundColor,
									restoreResponseObject.NoteCategories.get(j).Notes
											.get(noteIndex).NoteCreationDate,
									restoreResponseObject.NoteCategories.get(j).Notes
											.get(noteIndex).NoteType,
									tempNoteCategory,
									Constants.NONEXISTENT_NOTE_ID);

							if (restoreResponseObject.NoteCategories.get(j).Notes
									.get(noteIndex).ChecklistItemsResponseItems != null
									&& !restoreResponseObject.NoteCategories
											.get(j).Notes.get(noteIndex).ChecklistItemsResponseItems
											.isEmpty() && tempNoteID > 0) {
								for (int checklistIndex = 0; checklistIndex < restoreResponseObject.NoteCategories
										.get(j).Notes.get(noteIndex).ChecklistItemsResponseItems
										.size(); checklistIndex++) {
									addCheckListRows(
											tempNoteID,
											restoreResponseObject.NoteCategories
													.get(j).Notes
													.get(noteIndex).ChecklistItemsResponseItems
													.get(checklistIndex).ChecklistItemStatus,
											restoreResponseObject.NoteCategories
													.get(j).Notes
													.get(noteIndex).ChecklistItemsResponseItems
													.get(checklistIndex).ChecklistItemTitle);
								}

							}
						}
					}
				}
			}

			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}

	}

	public ArrayList<CategoryItems> getAllPasswordsCategories() {
		ArrayList<CategoryItems> items = new ArrayList<CategoryItems>();
		Cursor cursor = database
				.rawQuery(
						"select C._id , C.title , count(P._id)  from passwords_categories as C LEFT join passwords as P on C._id = P.password_categories_id group by  C._id ,C.title order by C.title COLLATE NOCASE ASC",
						null);
		if (cursor.moveToFirst()) {
			do {
				CategoryItems item = new CategoryItems();
				item.id = cursor.getInt(0);
				item.title = cursor.getString(1);
				item.count = cursor.getInt(2);
				item.icon = R.drawable.category;
				items.add(item);

			} while (cursor.moveToNext());

			cursor.close();
		}
		return items;

	}

	public int getNumberOfPasswordForCategory(int _categoryID) {
		int count = 0;
		String query = "SELECT _id FROM "
				+ DataBaseDecorator.TABLE_PASSWORDS[0] + " WHERE "
				+ DataBaseDecorator.TABLE_PASSWORDS[9] + " = " + _categoryID;
		Cursor cur = database.rawQuery(query, null);
		if (cur != null) {
			count = cur.getCount();
			cur.close();
		}

		return count;
	}

	// SYNC

	public List<NotesRequestItem> notesForCategory(int categoryID) {

		ArrayList<NotesRequestItem> notes = new ArrayList<NotesRequestItem>();
		Cursor cursor = database.rawQuery("select * from "
				+ DataBaseDecorator.TABLE_NOTES[0] + " WHERE "
				+ DataBaseDecorator.TABLE_NOTES[8] + " = " + categoryID
				+ " ORDER BY " + DataBaseDecorator.TABLE_NOTES[6]
				+ " COLLATE NOCASE DESC ", null);
		if (cursor.moveToFirst()) {
			do {
				NotesRequestItem item = new NotesRequestItem();
				int noteID = cursor.getInt(0);
				item.NoteTitle = cursor.getString(1);
				item.NoteText = cursor.getString(2);
				item.NoteBackgroundColor = cursor.getInt(4);
				item.NoteCreationDate = cursor.getInt(5);

				item.NoteType = cursor.getInt(6);
				if (cursor.getInt(6) == NoteItem.NOTE_TYPE_CHECKLIST) {
					item.ChecklistItemsRequestItem = syncGetChecklistItems(noteID);
				}
				notes.add(item);

			} while (cursor.moveToNext());

			cursor.close();
		}
		return notes;

	}

	public ArrayList<ChecklistItemsRequestItem> syncGetChecklistItems(int noteID) {
		ArrayList<ChecklistItemsRequestItem> items = new ArrayList<ChecklistItemsRequestItem>();
		Cursor cursor = database.rawQuery("select * from "
				+ DataBaseDecorator.TABLE_NOTES_CHKECLIST[0] + " WHERE "
				+ DataBaseDecorator.TABLE_NOTES_CHKECLIST[4] + " = " + noteID
				+ " ORDER BY " + DataBaseDecorator.TABLE_NOTES_CHKECLIST[1]
				+ " COLLATE NOCASE DESC ", null);
		if (cursor.moveToFirst()) {
			do {
				ChecklistItemsRequestItem item = new ChecklistItemsRequestItem();
				item.ChecklistItemTitle = cursor.getString(1);
				item.ChecklistItemStatus = cursor.getInt(2);

				items.add(item);

			} while (cursor.moveToNext());

			cursor.close();
		}
		return items;
	}

	public List<PasswordRequestItem> passwordsForCategory(int categoryID) {

		ArrayList<PasswordRequestItem> passwords = new ArrayList<PasswordRequestItem>();
		Cursor cursor = database.rawQuery("select * from "
				+ DataBaseDecorator.TABLE_PASSWORDS[0] + " WHERE "
				+ DataBaseDecorator.TABLE_PASSWORDS[9] + " = " + categoryID
				+ " order by " + DataBaseDecorator.TABLE_PASSWORDS[1]
				+ " desc ", null);
		if (cursor.moveToFirst()) {
			do {
				PasswordRequestItem item = new PasswordRequestItem();
				item.Title = cursor.getString(1);
				item.Account = cursor.getString(2);
				item.Text = cursor.getString(3);
				item.UserName = cursor.getString(4);
				item.Description = cursor.getString(5);
				item.Color = cursor.getInt(6);

				passwords.add(item);

			} while (cursor.moveToNext());

			cursor.close();
		}
		return passwords;

	}

	public List<PassCategoryRequestItem> getUserPasswords() {
		List<PassCategoriesDataModelObject> dbItems = new ArrayList<PassCategoriesDataModelObject>();
		List<PassCategoryRequestItem> passRequestItem = new ArrayList<PassCategoryRequestItem>();

		Cursor cursor = database
				.rawQuery(
						"select C._id , C.title , P.title , P.text ,  P.account , P.password_username , P.password_description , P.password_icon_bg , P._id from passwords_categories as C left join passwords as P on C._id = P.password_categories_id",

						null);
		if (cursor.moveToFirst()) {
			do {
				PassCategoriesDataModelObject item = new PassCategoriesDataModelObject();
				item.categoryID = cursor.getInt(0);
				item.categoryTitle = cursor.getString(1);
				item.passTitle = cursor.getString(2);
				item.passText = cursor.getString(3);

				item.passAccount = cursor.getString(4);
				item.passUserName = cursor.getString(5);
				item.passDescription = cursor.getString(6);

				item.passBackgroundColor = cursor.getInt(7);
				item.passwordID = cursor.getInt(8);

				dbItems.add(item);

			} while (cursor.moveToNext());

			cursor.close();
		}

		for (int i = 0; i < dbItems.size(); i++) {
			int dublicatedCatIndex = DataSourceUtilities.UNAVAILABLE_INDEX;
			for (int j = 0; j < passRequestItem.size(); j++) {
				if (passRequestItem.get(j).cateID == dbItems.get(i).categoryID) {

					dublicatedCatIndex = j;
					break;
				}
			}
			if (dublicatedCatIndex == DataSourceUtilities.UNAVAILABLE_INDEX) {
				passRequestItem.add(DataSourceUtilities.setPassDetails(
						dbItems.get(i), new PassCategoryRequestItem(), false));

			} else {
				DataSourceUtilities.setPassDetails(dbItems.get(i),
						passRequestItem.get(dublicatedCatIndex), true);
				dublicatedCatIndex = DataSourceUtilities.UNAVAILABLE_INDEX;
			}

		}

		return passRequestItem;

	}

	public List<NoteCategoryRequestItem> getUserNotes() {

		ArrayList<NoteCategoryRequestItem> userNotes = new ArrayList<NoteCategoryRequestItem>();
		Cursor cursor = database.rawQuery("select * from "
				+ DataBaseDecorator.TABLE_NOTES_CATEGORIES[0], null);
		if (cursor.moveToFirst()) {
			do {
				NoteCategoryRequestItem item = new NoteCategoryRequestItem();
				int categoryID = cursor.getInt(0);
				item.NoteCategoryName = cursor.getString(1);
				item.Notes = notesForCategory(categoryID);

				userNotes.add(item);

			} while (cursor.moveToNext());

			cursor.close();
		}
		return userNotes;

	}

	// PICTURES
	public int changePicturesCategoryName(int categoryID, String newCategoryName) {

		ContentValues value = new ContentValues();
		value.put(DataBaseDecorator.TABLE_PICTURES_CATEGORIES[2],
				newCategoryName);
		return (int) database.update(
				DataBaseDecorator.TABLE_PICTURES_CATEGORIES[0], value,
				DataBaseDecorator.TABLE_PICTURES_CATEGORIES[1] + " = "
						+ categoryID, null);

	}

	public boolean deletePicturesCategory(int categoryId) {

		if (database.delete(DataBaseDecorator.TABLE_PICTURES_CATEGORIES[0],
				DataBaseDecorator.TABLE_PICTURES_CATEGORIES[1] + " = ?",
				new String[] { String.valueOf(categoryId) }) != 0) {
			return true;

		}
		return true;

	}

	public int createNewPicturesCategories(String title) {
		if (!checkIfPicturesCategoryTitleExist(title)) {
			ContentValues values = new ContentValues();
			values.put(DataBaseDecorator.TABLE_PICTURES_CATEGORIES[2], title);

			return (int) database.insert(
					DataBaseDecorator.TABLE_PICTURES_CATEGORIES[0], null,
					values);

		} else {
			return -2;
		}
	}

	public boolean checkIfPicturesCategoryTitleExist(String albumName) {
		String query = "select * from "
				+ DataBaseDecorator.TABLE_PICTURES_CATEGORIES[0] + " where "
				+ DataBaseDecorator.TABLE_PICTURES_CATEGORIES[2] + " = ?; ";
		Cursor cursor = database.rawQuery(query, new String[] { albumName });
		if (cursor.getCount() > 0) {
			return true;

		} else {

			return false;
		}

	}

	public ArrayList<CategoryItems> getPicturesCategories(int iconID) {
		ArrayList<CategoryItems> items = new ArrayList<CategoryItems>();
		Cursor cursor = database
				.rawQuery(
						"select C._id , C.title , count (P._id)  from  pictures_categories as C left join pictures as P on C._id = P.picture_categories_id group by C._id , C.title order by C.title COLLATE NOCASE ASC",
						null);
		if (cursor.moveToFirst()) {
			do {
				CategoryItems item = new CategoryItems();
				item.id = cursor.getInt(0);
				item.title = cursor.getString(1);
				item.count = cursor.getInt(2);
				item.icon = iconID;
				items.add(item);
			} while (cursor.moveToNext());

			cursor.close();
		}
		return items;

	}

	public int getNumberOfPicturesForCategory(int categoryID) {
		int count = 0;
		String query = "SELECT _id FROM " + DataBaseDecorator.TABLE_PICTURES[0]
				+ " WHERE " + DataBaseDecorator.TABLE_PICTURES[3] + " = "
				+ categoryID;
		Cursor cur = database.rawQuery(query, null);
		if (cur != null) {
			count = cur.getCount();
			cur.close();
		}

		return count;
	}

	public int getNumberOfNotesForCategory(int categoryID) {
		int count = 0;
		String query = "SELECT _id FROM " + DataBaseDecorator.TABLE_NOTES[0]
				+ " WHERE " + DataBaseDecorator.TABLE_NOTES[8] + " = "
				+ categoryID;
		Cursor cur = database.rawQuery(query, null);
		if (cur != null) {
			count = cur.getCount();
			cur.close();
		}

		return count;
	}

	public List<String> getPicturesTitlesForCategory(int albumID) {
		List<String> pictures = new ArrayList<String>();
		String query = "SELECT " + DataBaseDecorator.TABLE_PICTURES[2]
				+ " FROM " + DataBaseDecorator.TABLE_PICTURES[0] + " WHERE "
				+ DataBaseDecorator.TABLE_PICTURES[3] + " = " + albumID;
		Cursor cur = database.rawQuery(query, null);
		if (cur.moveToFirst()) {
			do {
				String picture;
				picture = cur.getString(0);
				pictures.add(picture);
			} while (cur.moveToNext());

			cur.close();
		}

		return pictures;
	}

	public int addNewPicture(String albumName, int albumID) {

		ContentValues values = new ContentValues();
		values.put(DataBaseDecorator.TABLE_PICTURES[2], albumName);
		values.put(DataBaseDecorator.TABLE_PICTURES[3], albumID);
		return (int) database.insert(DataBaseDecorator.TABLE_PICTURES[0], null,
				values);
	}

	public int deletePictureByName(String imageName) {
		return (int) database.delete(DataBaseDecorator.TABLE_PICTURES[0],
				DataBaseDecorator.TABLE_PICTURES[2] + " = ?",
				new String[] { imageName });
	}

	@Override
	public void closeDatabaBase() {
		super.close();

	}

	@Override
	public void openDataBAse() {
		super.open();

	}

	@Override
	public boolean isDbOpen() {
		// TODO Auto-generated method stub
		return super.isDatabseOpen();
	}

	@Override
	public int createNewNoteCategory(String title) {
		ContentValues values = new ContentValues();
		values.put(DataBaseDecorator.TABLE_NOTES_CATEGORIES[2], title);
		return (int) database.insert(
				DataBaseDecorator.TABLE_NOTES_CATEGORIES[0], null, values);
	}

	@Override
	public ArrayList<CategoryItems> getNotesCategories(int iconID) {
		ArrayList<CategoryItems> items = new ArrayList<CategoryItems>();
		Cursor cursor = database
				.rawQuery(
						"select C._id , C.title , count(N._id) from  notes_categories as C left join notes as N on C._id = N.notes_categories_id group by C._id, C.title order by C.title COLLATE NOCASE ASC",
						null);
		if (cursor.moveToFirst()) {
			do {
				CategoryItems item = new CategoryItems();
				item.id = cursor.getInt(0);
				item.title = cursor.getString(1);
				item.count = cursor.getInt(2);
				item.icon = iconID;
				items.add(item);
			} while (cursor.moveToNext());

			cursor.close();
		}
		return items;
	}

	public ArrayList<NoteCategoryRequestItem> geNotesForCategory() {
		ArrayList<NoteCategoriesDataModelObject> DbListOfNotes = new ArrayList<NoteCategoriesDataModelObject>();
		ArrayList<NoteCategoryRequestItem> notes = new ArrayList<NoteCategoryRequestItem>();
		Cursor cursor = database
				.rawQuery(
						"select CA._id , CA.title ,  N._id , N.title , N.text , N.miniText , N.bgColor , N.modification_date , N.note_type  , C._id , C.title , C.status  from notes_categories as CA "
								+ " left join notes as N on CA._id = N.notes_categories_id  left join note_checklist as C  on  N._id = C.note_id order by C._id COLLATE NOCASE ASC",
						null);

		if (cursor.moveToFirst()) {
			do {
				NoteCategoriesDataModelObject item = new NoteCategoriesDataModelObject();
				item.categoryID = cursor.getInt(0);
				item.categoryTitle = cursor.getString(1);

				item.noteID = cursor.getInt(2);
				item.Title = cursor.getString(3);
				item.Text = cursor.getString(4);
				item.Minitext = cursor.getString(5);
				item.BackgroundColor = cursor.getInt(6);
				item.creationDate = cursor.getLong(7);
				item.type = cursor.getInt(8);
				item.checklistID = cursor.getInt(9);
				item.checklistText = cursor.getString(10);
				item.checklistStatus = cursor.getInt(11);
				DbListOfNotes.add(item);

			} while (cursor.moveToNext());

			cursor.close();

			for (int i = 0; i < DbListOfNotes.size(); i++) {
				int cateDuplicatedIndex = DataSourceUtilities.UNAVAILABLE_INDEX;
				int noteDuplicatedIndex = DataSourceUtilities.UNAVAILABLE_INDEX;
				for (int j = 0; j < notes.size(); j++) {
					if (DbListOfNotes.get(i).categoryID == notes.get(j).categoryID) {
						cateDuplicatedIndex = j;
						break;
					}
				}
				if (cateDuplicatedIndex != DataSourceUtilities.UNAVAILABLE_INDEX) {
					for (int c = 0; c < notes.get(cateDuplicatedIndex).Notes
							.size(); c++) {
						if (notes.get(cateDuplicatedIndex).Notes.get(c).noteID == DbListOfNotes
								.get(i).noteID) {
							noteDuplicatedIndex = c;
							break;
						}
					}

					DataSourceUtilities.setNoteDetail(
							notes.get(cateDuplicatedIndex),
							DbListOfNotes.get(i), cateDuplicatedIndex,
							noteDuplicatedIndex);

				}

				else {

					notes.add(DataSourceUtilities.setNoteDetail(
							new NoteCategoryRequestItem(),
							DbListOfNotes.get(i), cateDuplicatedIndex,
							noteDuplicatedIndex));

				}

			}

		}
		return notes;
	}

	public ArrayList<NoteItem> geNotessForCategory(int categoryID) {
		ArrayList<NoteItem> notes = new ArrayList<NoteItem>();
		Cursor cursor = database.rawQuery("select * from  "
				+ DataBaseDecorator.TABLE_NOTES[0] + " WHERE "
				+ DataBaseDecorator.TABLE_NOTES[8] + " = " + categoryID
				+ " ORDER BY " + DataBaseDecorator.TABLE_NOTES[6]
				+ " COLLATE NOCASE DESC ", null);
		if (cursor.moveToFirst()) {
			do {
				NoteItem item = new NoteItem();
				item.itemID = cursor.getInt(0);
				item.itemTitle = cursor.getString(1);
				item.itemText = cursor.getString(2);
				item.noteMiniText = cursor.getString(3);
				item.itemBackgroundColor = cursor.getInt(4);
				item.itemCreationDate = cursor.getLong(5);

				item.noteType = cursor.getInt(6);

				item.itemCategoryID = cursor.getInt(7);
				notes.add(item);

			} while (cursor.moveToNext());

			cursor.close();
		}
		return notes;
	}

	public int creatNewNoteWithoutEncry(String noteTitle, String noteText,
			String textFirst100Characters, int notBg, long date, int noteType,
			int categoryID, int originalNoteID) {

		ContentValues values = new ContentValues();
		values.put(DataBaseDecorator.TABLE_NOTES[2], noteTitle);
		values.put(DataBaseDecorator.TABLE_NOTES[3], noteText);
		values.put(DataBaseDecorator.TABLE_NOTES[4], textFirst100Characters);

		values.put(DataBaseDecorator.TABLE_NOTES[5], notBg);
		values.put(DataBaseDecorator.TABLE_NOTES[6], date);
		values.put(DataBaseDecorator.TABLE_NOTES[7], noteType);
		values.put(DataBaseDecorator.TABLE_NOTES[8], categoryID);

		int noteID = (int) database.insert(DataBaseDecorator.TABLE_NOTES[0],
				null, values);

		if (originalNoteID != Constants.NONEXISTENT_NOTE_ID) {
			database.delete(DataBaseDecorator.TABLE_NOTES[0],
					DataBaseDecorator.TABLE_NOTES[1] + " = ?",
					new String[] { String.valueOf(originalNoteID) });
		}

		return noteID;
	}

	public NoteItem getNoteDetails(int noteID) {
		NoteItem item = new NoteItem();
		String query = "SELECT * FROM " + DataBaseDecorator.TABLE_NOTES[0]
				+ " WHERE " + DataBaseDecorator.TABLE_NOTES[1] + " = " + noteID;
		Cursor cursor = database.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			item.itemTitle = cursor.getString(1);
			item.itemText = cursor.getString(2);
			item.noteMiniText = cursor.getString(3);
			item.itemBackgroundColor = cursor.getInt(4);
			item.itemCreationDate = cursor.getInt(5);
			item.noteType = cursor.getInt(6);
			if (item.noteType == NoteItem.NOTE_TYPE_CHECKLIST) {
				item.noteChecklist = getChecklistItems(noteID);
			}

		}
		cursor.close();
		return item;

	}

	public NoteItem getChecklistDetails(int checklistID) {
		NoteItem item = new NoteItem();
		List<SpecificNoteDataModelObject> checklistComponents = new ArrayList<SpecificNoteDataModelObject>();
		String query = "select N.title , N.bgColor , N.modification_date , C._id , C.title , C.status  from notes as N left join note_checklist as C on N._id = C.note_id where N._id = "
				+ checklistID + " order by C._id COLLATE NOCASE DESC";
		Cursor cursor = database.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			do {
				SpecificNoteDataModelObject checklist = new SpecificNoteDataModelObject();
				checklist.Title = cursor.getString(0);
				checklist.BackgroundColor = cursor.getInt(1);
				checklist.creationDate = cursor.getLong(2);
				checklist.checklistID = cursor.getInt(3);
				checklist.checklistText = cursor.getString(4);
				checklist.checklistStatus = cursor.getInt(5);
				checklistComponents.add(checklist);
			} while (cursor.moveToNext());
		}
		cursor.close();
		item.itemBackgroundColor = checklistComponents.get(0).BackgroundColor;
		item.itemCreationDate = checklistComponents.get(0).creationDate;
		item.itemTitle = checklistComponents.get(0).Title;
		for (int i = 0; i < checklistComponents.size(); i++) {
			ChecklistItem itemToAdd = new ChecklistItem();
			if (item.noteChecklist == null)
				item.noteChecklist = new ArrayList<NoteItem.ChecklistItem>();
			itemToAdd.ID = checklistComponents.get(i).checklistID;
			itemToAdd.status = checklistComponents.get(i).checklistStatus;
			itemToAdd.text = checklistComponents.get(i).checklistText;
			item.noteChecklist.add(itemToAdd);
		}
		return item;

	}

	public void creatNewChecklistWithoutEncry(String noteTitle, int notBg,
			long date, int noteType, int categoryID,
			List<ChecklistItem> checklistRows, int originalNoteID) {

		ContentValues values = new ContentValues();

		values.put(DataBaseDecorator.TABLE_NOTES[2], noteTitle);
		values.put(DataBaseDecorator.TABLE_NOTES[3], "");
		values.put(DataBaseDecorator.TABLE_NOTES[4], "");

		values.put(DataBaseDecorator.TABLE_NOTES[5], notBg);
		values.put(DataBaseDecorator.TABLE_NOTES[6], date);
		values.put(DataBaseDecorator.TABLE_NOTES[7], noteType);
		values.put(DataBaseDecorator.TABLE_NOTES[8], categoryID);

		int noteID = (int) database.insert(DataBaseDecorator.TABLE_NOTES[0],
				null, values);
		for (ChecklistItem item : checklistRows) {
			addCheckListRows(noteID, item.status,
					Utilities.removeSpacesFromText(item.text));
		}
		if (originalNoteID != Constants.NONEXISTENT_NOTE_ID) {
			database.delete(DataBaseDecorator.TABLE_NOTES[0],
					DataBaseDecorator.TABLE_NOTES[1] + " = ?",
					new String[] { String.valueOf(originalNoteID) });
		}
	}

	public void addCheckListRows(int noteID, int status, String text) {

		ContentValues values = new ContentValues();
		values.put(DataBaseDecorator.TABLE_NOTES_CHKECLIST[2], text);
		values.put(DataBaseDecorator.TABLE_NOTES_CHKECLIST[3], status);
		values.put(DataBaseDecorator.TABLE_NOTES_CHKECLIST[4], noteID);

		database.insert(DataBaseDecorator.TABLE_NOTES_CHKECLIST[0], null,
				values);

	}

	public ArrayList<ChecklistItem> getChecklistItems(int noteID) {
		ArrayList<ChecklistItem> items = new ArrayList<ChecklistItem>();
		Cursor cursor = database.rawQuery("select * from "
				+ DataBaseDecorator.TABLE_NOTES_CHKECLIST[0] + " WHERE "
				+ DataBaseDecorator.TABLE_NOTES_CHKECLIST[4] + " = " + noteID
				+ " ORDER BY " + DataBaseDecorator.TABLE_NOTES_CHKECLIST[1]
				+ " COLLATE NOCASE DESC ", null);
		if (cursor.moveToFirst()) {
			do {
				ChecklistItem item = new ChecklistItem();
				item.ID = cursor.getInt(0);
				item.text = cursor.getString(1);
				item.status = cursor.getInt(2);

				items.add(item);

			} while (cursor.moveToNext());

			cursor.close();
		}
		return items;
	}

	@Override
	public List<ChangeAppPassObject> getPasswordsText() {
		ArrayList<ChangeAppPassObject> items = new ArrayList<ChangeAppPassObject>();
		Cursor cursor = database.rawQuery("select "
				+ DataBaseDecorator.TABLE_PASSWORDS[1] + " , "
				+ DataBaseDecorator.TABLE_PASSWORDS[4] + " from "
				+ DataBaseDecorator.TABLE_PASSWORDS[0] + " ORDER BY "
				+ DataBaseDecorator.TABLE_PASSWORDS[1]
				+ " COLLATE NOCASE DESC ", null);

		if (cursor.moveToFirst()) {
			do {
				ChangeAppPassObject item = new ChangeAppPassObject();
				item.passwordID = cursor.getInt(0);
				item.passwordText = Cryptography.decrypt(secretKeySpec,
						cursor.getString(1));

				items.add(item);

			} while (cursor.moveToNext());

			cursor.close();
		}
		return items;
	}

	@Override
	public void updateAllPasstext(List<ChangeAppPassObject> items,
			SecretKeySpec secretKeySpec, String passKey) {
		database.beginTransaction();
		try {
			for (ChangeAppPassObject item : items) {
				updatePassText(item.passwordText, item.passwordID);
			}
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
			this.secretKeySpec = secretKeySpec;
			setMainAppPassword(Cryptography.hashMD5(passKey));
		}

	}

	public void updatePassText(String passwordText, int passID) {

		ContentValues values = new ContentValues();
		values.put(DataBaseDecorator.TABLE_PASSWORDS[4], passwordText);

		database.update(DataBaseDecorator.TABLE_PASSWORDS[0], values,
				DataBaseDecorator.TABLE_PASSWORDS[4] + " = " + passID, null);

	}

	@Override
	public int getBrowserCount() {
		String query = "SELECT * FROM web_text";

		Cursor cursor = database.rawQuery(query, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}

	@Override
	public void addNewWebText(String webText) {

		ContentValues values = new ContentValues();
		values.put(DataBaseDecorator.TABLE_WEB_TEXT[2], webText);
		values.put(DataBaseDecorator.TABLE_WEB_TEXT[3],
				CommonUtilities.timeInSecond());

		database.insert(DataBaseDecorator.TABLE_WEB_TEXT[0], null, values);
		if (getBrowserCount() > 50) {
			deleteLastWebTextRow();
		}

	}

	private int getLastRowID() {
		String query = "SELECT _id FROM web_text ORDER BY _id ASC LIMIT 1";
		Cursor cursor = database.rawQuery(query, null);
		int lastID = -1;
		if (cursor.moveToFirst()) {
			do {
				lastID = cursor.getInt(0);

			} while (cursor.moveToNext());

			cursor.close();
		}
		return lastID;
	}

	private void deleteLastWebTextRow() {
		int rowID = getLastRowID();
		if (rowID != -1)
		database.delete(DataBaseDecorator.TABLE_WEB_TEXT[0],
				DataBaseDecorator.TABLE_WEB_TEXT[1] + " = ?",
				new String[] { String.valueOf(rowID) });

	}

	@Override
	public List<BaseItem> getAllWebTexts() {
		ArrayList<BaseItem> items = new ArrayList<BaseItem>();
		Cursor cursor = database.rawQuery(
				"select text , date from "
						+ DataBaseDecorator.TABLE_WEB_TEXT[0] + " ORDER BY "
						+ DataBaseDecorator.TABLE_WEB_TEXT[1]
						+ " COLLATE NOCASE DESC ", null);

		if (cursor.moveToFirst()) {
			do {
				BaseItem item = new BaseItem(cursor.getString(0),
						cursor.getLong(1));

				items.add(item);

			} while (cursor.moveToNext());

			cursor.close();
		}
		return items;

	}
	
	public void deleteWebTexts() {
		database.execSQL("delete from "
				+ DataBaseDecorator.TABLE_WEB_TEXT[0]);
		
	}

}

