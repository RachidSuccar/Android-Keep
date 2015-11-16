package com.funnel.keep.decorator;

import com.funnel.keep.sqlDataBaseManager.SQLiteHelper;

public class DataBaseDecorator {

	public static final String DATABASE_NAME = "KeepSafeDataBaseV23.db";
	public static final int DATABASE_VERSION = SQLiteHelper.VERSION_WEB;
	public final static String[] TABLE_SETTINGS = { "setting", "_id",
			"app_password_hashed" };

	// PASSWORDS
	public final static String[] TABLE_PASSWORDS_CATEGORIES = {
			"passwords_categories", "_id", "title" };

	public final static String[] TABLE_PASSWORDS = { "passwords", "_id",
			"title", "account", "text", "password_username",
			"password_description", "password_icon_bg", "date_added",
			"password_categories_id" };
	// PICTURES
	public final static String[] TABLE_PICTURES_CATEGORIES = {
			"pictures_categories", "_id", "title" };
	public final static String[] TABLE_PICTURES = { "pictures", "_id", "title",
			"picture_categories_id" };

	// NOTES
	public final static String[] TABLE_NOTES_CATEGORIES = { "notes_categories",
			"_id", "title" };
	public final static String[] TABLE_NOTES = { "notes", "_id", "title",
			"text", "miniText", "bgColor", "modification_date", "note_type",
			"notes_categories_id" };
	// Note Checklist
	public final static String[] TABLE_NOTES_CHKECLIST = { "note_checklist",
			"_id", "title", "status", "note_id" };
	
	// Note browser
	public final static String[] TABLE_BROWSER = { "browser",
				"_id", "fingerprint"};
	
	// Note web_text
	public final static String[] TABLE_WEB_TEXT = { "web_text",
					"_id", "text" , "date"};

}
