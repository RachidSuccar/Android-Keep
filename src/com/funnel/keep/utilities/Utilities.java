package com.funnel.keep.utilities;

import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;

public class Utilities {
	

	public static void clearPreferences(Context context) {
		// reset all application preferences
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.clear();
		editor.commit();
	}

	public static String removeSpacesFromText(String text) {
		// trim + replace multiple spaces with on space
		return text.trim().replaceAll("\\s+", " ");
	}

	public static String getDate(double Dtime) {
		long time = (long) Dtime;
		Date date = new Date(time * 1000);

		String dateFormat = DateFormat.format("E, MMMM d, yyyy", date)
				.toString();
		return dateFormat;
	}

	public static boolean checkIfStringContainNumber(String textToTest) {

		Pattern p = Pattern.compile("([0-9])");
		Matcher m = p.matcher(textToTest);

		return m.find();

	}

	public static boolean checkIfStringCharacters(String textToTest) {

		Pattern p = Pattern.compile("([[a-z][A-Z]])");
		Matcher m = p.matcher(textToTest);

		return m.find();

	}

}
