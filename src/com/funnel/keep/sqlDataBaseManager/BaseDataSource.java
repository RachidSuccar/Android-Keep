package com.funnel.keep.sqlDataBaseManager;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public abstract class BaseDataSource implements IDataAccess {

	protected SQLiteDatabase database;
	protected SQLiteHelper dbHelper;

	public abstract void closeDatabaBase();

	public abstract void openDataBAse();

	public abstract boolean isDbOpen();

	protected boolean isDatabseOpen() {
		if (database != null) {
			return database.isOpen();
		} else {
			return false;
		}

	}

	protected void open() throws SQLException {
		database = dbHelper.getWritableDatabase();

	}

	protected void close() {
		dbHelper.close();
	}

}
