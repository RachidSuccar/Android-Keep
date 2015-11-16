package com.funnel.keep.sqlDataBaseManager;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import com.funnel.keep.base.BaseItem;
import com.funnel.keep.requestObjects.NoteCategoryRequestItem;
import com.funnel.keep.requestObjects.PassCategoryRequestItem;
import com.funnel.keep.requestObjects.PasswordRequestItem;
import com.funnel.keep.responseObject.RestoreResponseObject;
import com.funnel.keep.decorator.CategoryItems;
import com.funnel.keep.decorator.ChangeAppPassObject;
import com.funnel.keep.decorator.NoteItem;
import com.funnel.keep.decorator.NoteItem.ChecklistItem;
import com.funnel.keep.decorator.PasswordItem;

public interface IDataAccess {
	void deleteWebTexts();

	List<BaseItem> getAllWebTexts();

	void addNewWebText(String webText);

	void setMainAppPassword(String password);

	String getPassword();

	int creatNewPassword(String passwordTitle, String passwordAccount,
			String passwordText, String passwordUserName,
			String passwordDescription, int passwordIconbg, long date,
			int categoryID);

	int creatNewPasswordWithoutEncry(String passwordTitle,
			String passwordAccount, String passwordText,
			String passwordUserName, String passwordDescription,
			int passwordIconbg, long date, int categoryID);

	ArrayList<PasswordItem> gePasswordsForCategory(int categoryID);

	ArrayList<NoteCategoryRequestItem> geNotesForCategory();

	int deletePassword(int id);

	PasswordItem getPasswordDetails(int passwordID);

	int updatePassword(int passwordID, String passwordTitle,
			String passwordAccount, String passwordText,
			String passwordUserName, String passwordDescription,
			int passwordIconbg);

	int deletePasswordsForCategoryID(int categoryID);

	int changePasswordsCategoryName(int categoryID, String newCategoryName);

	int createNewPasswordsCategory(String title);

	int createNewNoteCategory(String title);

	boolean deletePasswordsCategory(int categoryId);

	void deleteUserInfos();

	ArrayList<CategoryItems> getAllPasswordsCategories();

	int getNumberOfPasswordForCategory(int _categoryID);

	List<PasswordRequestItem> passwordsForCategory(int categoryID);

	// List<PassCategoryRequestItem> getAllUserInfo();

	boolean CheckIfTableisEmpty(String tableName);

	boolean checkIfPicturesCategoryTitleExist(String albumName);

	int createNewPicturesCategories(String albumName);

	ArrayList<CategoryItems> getPicturesCategories(int iconID);

	ArrayList<CategoryItems> getNotesCategories(int iconID);

	int addNewPicture(String albumName, int albumID);

	List<String> getPicturesTitlesForCategory(int albumID);

	int deletePictureByName(String imageName);

	int changePicturesCategoryName(int albumID, String newCategoryName);

	boolean deletePicturesCategory(int categoryId);

	int creatNewNoteWithoutEncry(String noteTitle, String noteText,
			String textFirst100Characters, int notBg, long date, int noteType,
			int categoryID, int originalNoteID);

	NoteItem getNoteDetails(int noteID);

	void creatNewChecklistWithoutEncry(String noteTitle, int notBg, long date,
			int noteType, int categoryID, List<ChecklistItem> checklistRows,
			int originalNoteID);

	boolean deleteNoteCategory(int categoryId);

	int deleteNote(int noteID);

	int changeNoteCategoryName(int categoryID, String newCategoryName);

	List<NoteCategoryRequestItem> getUserNotes();

	List<PassCategoryRequestItem> getUserPasswords();

	List<ChangeAppPassObject> getPasswordsText();

	void updateAllPasstext(List<ChangeAppPassObject> items,
			SecretKeySpec secretKeySpec, String passKey);

	NoteItem getChecklistDetails(int checklistID);

	ArrayList<NoteItem> geNotessForCategory(int categoryID);

	void restoreUserInfos(RestoreResponseObject restoreResponseObject,
			SecretKeySpec secretKeySpec);

	int getBrowserCount();
}
