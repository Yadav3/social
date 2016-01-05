package com.etisbew.eatz.common;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.etisbew.eatz.objects.CitiesDO;
import com.etisbew.eatz.objects.LocationDO;

public class Appconstants {

	// new arrays for load more concept
	public static ArrayList<Integer> new_venueid = new ArrayList<Integer>();
	public static ArrayList<String> new_venuename = new ArrayList<String>();
	public static ArrayList<String> new_image = new ArrayList<String>();
	public static ArrayList<String> new_location = new ArrayList<String>();

	public static ArrayList<String> new_address1 = new ArrayList<String>();
	public static ArrayList<String> new_address2 = new ArrayList<String>();
	public static ArrayList<String> new_review_rating = new ArrayList<String>();
	public static ArrayList<String> new_review_count = new ArrayList<String>();

	public static ArrayList<String> new_cuisines_list = new ArrayList<String>();
	public static ArrayList<String> new_serving_timings = new ArrayList<String>();
	public static ArrayList<Boolean> new_happy_hours = new ArrayList<Boolean>();
	public static ArrayList<String> new_happy_hours_start = new ArrayList<String>();

	public static ArrayList<String> new_happy_hours_end = new ArrayList<String>();
	public static ArrayList<String> new_has_sundayBrunch = new ArrayList<String>();
	public static ArrayList<String> new_next_sunday_brunch = new ArrayList<String>();
	public static ArrayList<String> new_brunch_description = new ArrayList<String>();

	public static ArrayList<String> new_existing_buffets = new ArrayList<String>();
	public static ArrayList<String> new_openstatus = new ArrayList<String>();
	public static ArrayList<Double> new_currentdistance = new ArrayList<Double>();

	/*
	 * public static String LATTITUDE = "17.4406"; public static String
	 * LANGITUDE = "78.3911";
	 */
	public static String LATTITUDE;
	public static String LANGITUDE;
	public static final String DB_NAME = "EATZ";
	public static LocationDO ltDo = new LocationDO();
	public static String strCityId = "22960";
	public static String strCityName = "Hyderabad";
	public static String strCountryName = "India";
	public static String strPostalcode = "500048";
	public static String strMenuflag = "";
	public static String booktableres = "";
	public static String booktableSlotsres = "";
	public static String orderFoodres = "";
	public static String strPurchaseId = "";
	public static String strPurchaseSubUrl = "";
	public static String strFinalAmount = "";
	public static String strOrderType = "";

	public static String Strdatetime = "";
	public static String orders = "";
	public static String eachidprices = "";
	public static String eachidqty = "";
	public static String name = ""; 
	public static String address = "";
	
	public static String MAIN_HOST = "http://www.eatz.com/WebServices/";
	public static String PURCHASED_ITEMS_HOST = "http://www.eatz.com/";
	public static String url2 = "http://www.eatz.com/WebServices2/";
	public static String ARTICLE_IMG_BASE_URL = "http://www.eatz.com/img/article/";

	public static ArrayList<String> location_id = new ArrayList<String>();
	public static ArrayList<String> location_name = new ArrayList<String>();
	public static ArrayList<LocationDO> location_do = new ArrayList<LocationDO>();
	public static ArrayList<CitiesDO> cities_do = new ArrayList<CitiesDO>();

	public static ArrayList<String> location_array = new ArrayList<String>();
	public static ArrayList<String> location_lat = new ArrayList<String>();
	public static ArrayList<String> location_lang = new ArrayList<String>();
	public static ArrayList<String> locationID_array = new ArrayList<String>();

	public static ArrayList<String> india_city_id = new ArrayList<String>();
	public static ArrayList<String> india_city_name = new ArrayList<String>();
	public static ArrayList<String> india_city_lat = new ArrayList<String>();
	public static ArrayList<String> india_city_lang = new ArrayList<String>();

	public static ArrayList<String> uae_city_id = new ArrayList<String>();
	public static ArrayList<String> uae_city_name = new ArrayList<String>();
	public static ArrayList<String> uae_city_lat = new ArrayList<String>();
	public static ArrayList<String> uae_city_lang = new ArrayList<String>();

	public static String type = "";
	public static String strWebviewUrl = "";
	public static String[] location_ids = { "" };
	public static String[] location_names = { "" };

	public static final String APP_ID = "808633565894213";

	public static String userName = "";
	public static String email = "";
	public static String accessToken = "";
	public static String staticURL = "";
	public static String sessionId = "";
	public static String userId = "";
	public static String firstname = "";
	public static String phone = "";
	public static String email_ = "";
	public static int user_flag = 0;
	public static String filters_flg = "";

	static void showDialogMsg(Context con, String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(con);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
}
