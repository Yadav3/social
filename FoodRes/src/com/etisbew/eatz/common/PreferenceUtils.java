package com.etisbew.eatz.common;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {

	public static SharedPreferences pref;

	// private static Context ctx;

	@SuppressWarnings("static-access")
	public PreferenceUtils(Context context) {

		pref = context.getSharedPreferences("eatz", context.MODE_PRIVATE);
		// ctx = context;
	}

	public static String getUserSession() {

		String id = pref.getString("sessionId", "none");

		return id;

	}

	public static String getOrderSession() {

		String orderid = pref.getString("orderid", "0");

		return orderid;

	}

	public static String getOrderTypes() {

		String orderid = pref.getString("order_type", "0");

		return orderid;

	}

	public static void setCityName(String value) {

		pref.edit().putString("cityname", value).commit();

	}

	public static String getCityName() {

		return pref.getString("cityname", "none");

	}

	public static String getUserName1() {

		String id = pref.getString("strUserName", "none");

		return id;

	}

	public static String getUserPhone1() {// --------------------

		String id = pref.getString("strUserPhone", "");

		return id;

	}

	public static String getUserAddress1() {

		String id = pref.getString("strUserAddress", "");

		return id;

	}

	public static String getUserEmail1() {

		String id = pref.getString("strUserEmail", "");

		return id;

	}

	public static String getUserId() { // ------------

		String id = pref.getString("userId", "none");

		return id;

	}

	public static void setUserId(String value) {

		pref.edit().putString("userId", value).commit();

	}

	public static void setUserSession(String value) {

		pref.edit().putString("sessionId", value).commit();

	}

	public static void setOrderSession(String value) {

		pref.edit().putString("orderid", value).commit();

	}

	public static void setOrderTypes(String value) {

		pref.edit().putString("order_type", value).commit();

	}

	public static void setUserName1(String value) {

		pref.edit().putString("strUserName", value).commit();

	}

	public static void setUserPhone1(String value) {

		pref.edit().putString("strUserPhone", value).commit();

	}

	public static void setUserAddress1(String value) {

		pref.edit().putString("strUserAddress", value).commit();

	}

	public static void setUserEmail1(String value) {

		pref.edit().putString("strUserEmail", value).commit();

	}

	public static void setUserProfilePic(String value) {

		pref.edit().putString("profilePic", value).commit();

	}

	public static String getUserProfilePic() {

		return pref.getString("profilePic", "none");

	}

	public static void setUserFirstName(String value) {

		pref.edit().putString("firstname", value).commit();

	}

	public static String getUserFirstName() {

		return pref.getString("firstname", "none");

	}

	public static void setUserLastName(String value) {

		pref.edit().putString("lastname", value).commit();

	}

	public static String getUserLastName() {

		return pref.getString("lastname", "none");

	}

	//
	/*
	 * public static void setFBUserFirstName(String value) {
	 * 
	 * pref.edit().putString("firstname", value).commit();
	 * 
	 * }
	 * 
	 * public static String getFBUserFirstName() {
	 * 
	 * return pref.getString("firstname", "none");
	 * 
	 * }
	 * 
	 * public static void setFBUserLastName(String value) {
	 * 
	 * pref.edit().putString("lastname", value).commit();
	 * 
	 * }
	 * 
	 * public static String getFBUserLastName() {
	 * 
	 * return pref.getString("lastname", "none");
	 * 
	 * }
	 * 
	 * public static void setFBUserEmail(String value) {
	 * 
	 * pref.edit().putString("email", value).commit();
	 * 
	 * }
	 * 
	 * public static String getFBUserEmail() {
	 * 
	 * return pref.getString("email", "none");
	 * 
	 * }
	 */

	public static void removeUserName() {

		pref.edit().putString("sessionId", "none").commit();
		pref.edit().putString("strUserName", "none").commit();

		pref.edit().putString("strUserPhone", "none").commit();
		pref.edit().putString("strUserAddress", "none").commit();
		pref.edit().putString("strUserEmail", "none").commit();
		pref.edit().putString("userId", "none").commit();
		pref.edit().putString("order_type", "none").commit();
		pref.edit().putString("orderid", "none").commit();
		pref.edit().putString("firstname", "none").commit();
		pref.edit().putString("lastname", "none").commit();
		pref.edit().putString("userPoints", "none").commit();
		pref.edit().putString("userPointsPending", "none").commit();

		// SessionStore.clear(ctx);
		// Util.clearCookies(ctx);
		//
		// SharedPreferences settings = PreferenceManager
		// .getDefaultSharedPreferences(ctx);
		// SharedPreferences.Editor editor = settings.edit();
		// editor.remove("session");
		// editor.commit();

	}

	public static void setUserPoints(String value) {
		pref.edit().putString("userPoints", value).commit();
	}

	public static String getUserPoints() {
		String id = pref.getString("userPoints", "none");

		return id;
	}

	public static void setUserPointsPending(String value) {
		pref.edit().putString("userPointsPending", value).commit();
	}

	public static String getUserPointsPending() {
		String id = pref.getString("userPointsPending", "none");

		return id;
	}

	public static Integer getVersionCode() { // ------------

		int id = pref.getInt("versioncode", 0);

		return id;

	}

	public static void setVersionCode(int value) {

		pref.edit().putInt("versioncode", value).commit();

	}

}