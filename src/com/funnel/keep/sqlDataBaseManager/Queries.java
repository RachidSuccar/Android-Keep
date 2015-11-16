package com.funnel.keep.sqlDataBaseManager;

import com.funnel.keep.decorator.DataBaseDecorator;

public class Queries {

	public static final String CREATE_TABLE_SETTINGS = "create table "
			+ DataBaseDecorator.TABLE_SETTINGS[0] + "("
			+ DataBaseDecorator.TABLE_SETTINGS[1]
			+ " integer primary key autoincrement, "
			+ DataBaseDecorator.TABLE_SETTINGS[2] + " text not null);";

	public static final String CREATE_TABLE_PASSWORDS_CATEGORIES = "create table "
			+ DataBaseDecorator.TABLE_PASSWORDS_CATEGORIES[0]
			+ "("
			+ DataBaseDecorator.TABLE_PASSWORDS_CATEGORIES[1]
			+ " integer primary key autoincrement, "
			+ DataBaseDecorator.TABLE_PASSWORDS_CATEGORIES[2]
			+ " text not null); ";

	public static final String CREATE_TABLE_PASSWORDS = "create table "
			+ DataBaseDecorator.TABLE_PASSWORDS[0] + "("
			+ DataBaseDecorator.TABLE_PASSWORDS[1]
			+ " integer primary key autoincrement, "
			+ DataBaseDecorator.TABLE_PASSWORDS[2] + " text not null, "
			+ DataBaseDecorator.TABLE_PASSWORDS[3] + " text, "
			+ DataBaseDecorator.TABLE_PASSWORDS[4] + " text not null, "
			+ DataBaseDecorator.TABLE_PASSWORDS[5] + " text, "
			+ DataBaseDecorator.TABLE_PASSWORDS[6] + " text, "

			+ DataBaseDecorator.TABLE_PASSWORDS[7] + " integer, "

			+ DataBaseDecorator.TABLE_PASSWORDS[8] + " integer, "

			+ DataBaseDecorator.TABLE_PASSWORDS[9] + " integer,"

			+ " FOREIGN KEY (" + DataBaseDecorator.TABLE_PASSWORDS[9]
			+ ") REFERENCES " + DataBaseDecorator.TABLE_PASSWORDS_CATEGORIES[0]
			+ " (" + DataBaseDecorator.TABLE_PASSWORDS_CATEGORIES[1] + "));";

	public static final String CREATE_PICTURES_CATEGORIES = "create table "
			+ DataBaseDecorator.TABLE_PICTURES_CATEGORIES[0] + "("
			+ DataBaseDecorator.TABLE_PICTURES_CATEGORIES[1]
			+ " integer primary key autoincrement, "
			+ DataBaseDecorator.TABLE_PICTURES_CATEGORIES[2]
			+ " text not null); ";

	public static final String CREATE_TABLE_PICTURES = "create table "
			+ DataBaseDecorator.TABLE_PICTURES[0] + "("
			+ DataBaseDecorator.TABLE_PICTURES[1]
			+ " integer primary key autoincrement, "
			+ DataBaseDecorator.TABLE_PICTURES[2] + " text not null, "
			+ DataBaseDecorator.TABLE_PICTURES[3] + " integer,"
			+ " FOREIGN KEY (" + DataBaseDecorator.TABLE_PICTURES[3]
			+ ") REFERENCES " + DataBaseDecorator.TABLE_PICTURES_CATEGORIES[0]
			+ " (" + DataBaseDecorator.TABLE_PICTURES_CATEGORIES[1] + "));";

	public static final String CREATE_TABLE_NOTES_CATEGORIES = "create table "
			+ DataBaseDecorator.TABLE_NOTES_CATEGORIES[0] + "("
			+ DataBaseDecorator.TABLE_NOTES_CATEGORIES[1]
			+ " integer primary key autoincrement, "
			+ DataBaseDecorator.TABLE_NOTES_CATEGORIES[2] + " text not null); ";

	public static final String CREATE_TABLE_NOTES = "create table "
			+ DataBaseDecorator.TABLE_NOTES[0] + "("
			+ DataBaseDecorator.TABLE_NOTES[1]
			+ " integer primary key autoincrement, "
			+ DataBaseDecorator.TABLE_NOTES[2] + " text, "

			+ DataBaseDecorator.TABLE_NOTES[3] + " text, "
			+ DataBaseDecorator.TABLE_NOTES[4] + " text, "
			+ DataBaseDecorator.TABLE_NOTES[5] + " integer, "
			+ DataBaseDecorator.TABLE_NOTES[6] + " integer, "
			+ DataBaseDecorator.TABLE_NOTES[7] + " integer, "
			+ DataBaseDecorator.TABLE_NOTES[8] + " integer," + " FOREIGN KEY ("
			+ DataBaseDecorator.TABLE_NOTES[8] + ") REFERENCES "
			+ DataBaseDecorator.TABLE_NOTES_CATEGORIES[0] + " ("
			+ DataBaseDecorator.TABLE_NOTES_CATEGORIES[1] + "));";

	public static final String CREATE_TABLE_NOTE_CHEKLISTS = "create table "
			+ DataBaseDecorator.TABLE_NOTES_CHKECLIST[0] + "("
			+ DataBaseDecorator.TABLE_NOTES_CHKECLIST[1]
			+ " integer primary key autoincrement, "
			+ DataBaseDecorator.TABLE_NOTES_CHKECLIST[2] + " text, "

			+ DataBaseDecorator.TABLE_NOTES_CHKECLIST[3]
			+ " integer not null, "

			+ DataBaseDecorator.TABLE_NOTES_CHKECLIST[4] + " integer,"
			+ " FOREIGN KEY (" + DataBaseDecorator.TABLE_NOTES_CHKECLIST[4]
			+ ") REFERENCES " + DataBaseDecorator.TABLE_NOTES[0] + " ("
			+ DataBaseDecorator.TABLE_NOTES[1] + "));";

	public static final String CREATE_TABLE_BROWSER = "create table "
			+ DataBaseDecorator.TABLE_BROWSER[0] + "("
			+ DataBaseDecorator.TABLE_BROWSER[1]
			+ " integer primary key autoincrement, "
			+ DataBaseDecorator.TABLE_BROWSER[2] + " text not null); ";

	public static final String CREATE_TABLE_WEB_TEXT = "create table "
			+ DataBaseDecorator.TABLE_WEB_TEXT[0] + "("
			+ DataBaseDecorator.TABLE_WEB_TEXT[1]
			+ " integer primary key autoincrement, "
			+ DataBaseDecorator.TABLE_WEB_TEXT[2] + " text not null, "
			+ DataBaseDecorator.TABLE_WEB_TEXT[3] + " integer); ";

}