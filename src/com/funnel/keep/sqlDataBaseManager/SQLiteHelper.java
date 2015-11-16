package com.funnel.keep.sqlDataBaseManager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.funnel.keep.decorator.DataBaseDecorator;

public class SQLiteHelper extends SQLiteOpenHelper {
	
	
	public static final int VERSION_WEB = 3;

	public SQLiteHelper(Context context) {
		super(context, DataBaseDecorator.DATABASE_NAME, null,
				DataBaseDecorator.DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Queries.CREATE_TABLE_SETTINGS);
		db.execSQL(Queries.CREATE_TABLE_PASSWORDS_CATEGORIES);
		db.execSQL(Queries.CREATE_TABLE_PASSWORDS);
		db.execSQL(Queries.CREATE_PICTURES_CATEGORIES);
		db.execSQL(Queries.CREATE_TABLE_PICTURES);
		db.execSQL(Queries.CREATE_TABLE_NOTES_CATEGORIES);
		db.execSQL(Queries.CREATE_TABLE_NOTES);
		db.execSQL(Queries.CREATE_TABLE_NOTE_CHEKLISTS);
		db.execSQL(Queries.CREATE_TABLE_BROWSER);
		db.execSQL(Queries.CREATE_TABLE_WEB_TEXT);
		db.execSQL("PRAGMA foreign_keys=ON;");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		int updateTo = oldVersion + 1;
		switch (updateTo) {
		case 2:
		//do nothing
		case VERSION_WEB:
			db.execSQL(Queries.CREATE_TABLE_BROWSER);
			db.execSQL(Queries.CREATE_TABLE_WEB_TEXT);
			
		}

	}

}
