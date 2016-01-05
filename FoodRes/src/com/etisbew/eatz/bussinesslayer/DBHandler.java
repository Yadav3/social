package com.etisbew.eatz.bussinesslayer;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.objects.CitiesDO;
import com.etisbew.eatz.objects.LocationDO;

public class DBHandler extends SQLiteOpenHelper {

	public final String LOCATIONS = "LocationTable";
	public final String CITYTABLE = "CityTable";

	SQLiteDatabase db = null;

	public DBHandler(Context context) {
		super(context, Appconstants.DB_NAME, null, 2);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("create table if not exists " + LOCATIONS
				+ "(ID TEXT,NAME TEXT, LAT NAME, LANG NAME);");
		db.execSQL("create table if not exists "
				+ CITYTABLE
				+ "(CITY_ID TEXT,CITY_NAME TEXT, COUNTRY_NAME TEXT, LAT NAME, LANG NAME);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.rawQuery("drop table " + LOCATIONS, null);
		onCreate(db);

	}

	public void openDB() {
		db = this.getWritableDatabase();
	}

	public void closeDB() {

		if (db != null)
			db.close();
	}

	public ArrayList<LocationDO> getAllLocations() {

		openDB();
		ArrayList<LocationDO> ldLocation = new ArrayList<LocationDO>();

		Cursor cr = db.rawQuery("select * from " + LOCATIONS
				+ " order by NAME asc", null);
		if (cr.getCount() > 0) {
			while (cr.moveToNext()) {
				LocationDO ld = new LocationDO();
				ld.id = cr.getString(cr.getColumnIndex("ID"));
				ld.name = cr.getString(cr.getColumnIndex("NAME"));
				ld.lat = cr.getString(cr.getColumnIndex("LAT"));
				ld.lang = cr.getString(cr.getColumnIndex("LANG"));
				ld.cat = "loc";
				ldLocation.add(ld);

			}
		}

		closeDB();

		return ldLocation;

	}

	public ArrayList<String> getAllLocationsArray() {

		openDB();
		ArrayList<String> ldLocation = new ArrayList<String>();

		Cursor cr = db.rawQuery("select * from " + LOCATIONS
				+ " order by NAME asc", null);
		if (cr.getCount() > 0) {
			while (cr.moveToNext()) {
				ldLocation.add(cr.getString(cr.getColumnIndex("NAME")));
			}
		}

		closeDB();

		return ldLocation;

	}

	public ArrayList<String> getAllLatitude() {

		openDB();
		ArrayList<String> ldLocation = new ArrayList<String>();

		Cursor cr = db.rawQuery("select * from " + LOCATIONS
				+ " order by NAME asc", null);
		if (cr.getCount() > 0) {
			while (cr.moveToNext()) {
				ldLocation.add(cr.getString(cr.getColumnIndex("LAT")));
			}
		}

		closeDB();

		return ldLocation;

	}

	public ArrayList<String> getAllLangitude() {

		openDB();
		ArrayList<String> ldLocation = new ArrayList<String>();

		Cursor cr = db.rawQuery("select * from " + LOCATIONS
				+ " order by NAME asc", null);
		if (cr.getCount() > 0) {
			while (cr.moveToNext()) {
				ldLocation.add(cr.getString(cr.getColumnIndex("LANG")));
			}
		}

		closeDB();

		return ldLocation;

	}

	public ArrayList<String> getAllLocationIDs() {

		openDB();
		ArrayList<String> ldLocationID = new ArrayList<String>();

		Cursor cr = db.rawQuery("select * from " + LOCATIONS
				+ " order by NAME asc", null);
		if (cr.getCount() > 0) {
			while (cr.moveToNext()) {
				ldLocationID.add(cr.getString(cr.getColumnIndex("ID")));
			}
		}

		closeDB();
		return ldLocationID;

	}

	public ArrayList<String> getAllCountries() {

		openDB();
		ArrayList<String> ldCOUNTRY = new ArrayList<String>();

		Cursor cr = db.rawQuery("select distinct COUNTRY_NAME from "
				+ CITYTABLE + " order by COUNTRY_NAME asc", null);
		if (cr.getCount() > 0) {
			while (cr.moveToNext()) {
				ldCOUNTRY.add(cr.getString(cr.getColumnIndex("COUNTRY_NAME")));
			}
		}

		closeDB();
		return ldCOUNTRY;

	}

	public ArrayList<String> getAllCityNames(String countryname) {

		openDB();
		ArrayList<String> ldCOUNTRY = new ArrayList<String>();

		Cursor cr = db.rawQuery("select CITY_NAME from " + CITYTABLE
				+ " where COUNTRY_NAME='" + countryname
				+ "' order by CITY_NAME asc", null);
		if (cr.getCount() > 0) {
			while (cr.moveToNext()) {
				ldCOUNTRY.add(cr.getString(cr.getColumnIndex("CITY_NAME")));
			}
		}

		closeDB();
		return ldCOUNTRY;

	}

	public ArrayList<String> getAllLat(String countryname) {

		openDB();
		ArrayList<String> ldCOUNTRY = new ArrayList<String>();

		Cursor cr = db.rawQuery("select LAT from " + CITYTABLE
				+ " where COUNTRY_NAME='" + countryname
				+ "' order by CITY_NAME asc", null);
		if (cr.getCount() > 0) {
			while (cr.moveToNext()) {
				ldCOUNTRY.add(cr.getString(cr.getColumnIndex("LAT")));
			}
		}

		closeDB();
		return ldCOUNTRY;

	}

	public ArrayList<String> getAllLang(String countryname) {

		openDB();
		ArrayList<String> ldCOUNTRY = new ArrayList<String>();

		Cursor cr = db.rawQuery("select LANG from " + CITYTABLE
				+ " where COUNTRY_NAME='" + countryname
				+ "' order by CITY_NAME asc", null);
		if (cr.getCount() > 0) {
			while (cr.moveToNext()) {
				ldCOUNTRY.add(cr.getString(cr.getColumnIndex("LANG")));
			}
		}

		closeDB();
		return ldCOUNTRY;

	}

	public ArrayList<String> getAllCityId(String countryname) {

		openDB();
		ArrayList<String> ldCOUNTRY = new ArrayList<String>();

		Cursor cr = db.rawQuery("select CITY_ID from " + CITYTABLE
				+ " where COUNTRY_NAME='" + countryname
				+ "' order by CITY_NAME asc", null);
		if (cr.getCount() > 0) {
			while (cr.moveToNext()) {
				ldCOUNTRY.add(cr.getString(cr.getColumnIndex("CITY_ID")));
			}
		}
		closeDB();
		return ldCOUNTRY;

	}

	public void insertLocations(ArrayList<LocationDO> ltDo) {

		openDB();
		ContentValues cv = new ContentValues();

		for (int i = 0; i < ltDo.size(); i++) {
			cv.clear();
			cv.put("ID", ltDo.get(i).id);
			cv.put("NAME", ltDo.get(i).name);
			cv.put("LAT", ltDo.get(i).lat);
			cv.put("LANG", ltDo.get(i).lang);
			db.insert(LOCATIONS, null, cv);
		}
		closeDB();
	}

	public void insertCities(ArrayList<CitiesDO> ctDo) {

		openDB();
		ContentValues cv = new ContentValues();

		for (int i = 0; i < ctDo.size(); i++) {
			cv.clear();
			cv.put("CITY_ID", ctDo.get(i).id);
			cv.put("CITY_NAME", ctDo.get(i).city_name);
			cv.put("COUNTRY_NAME", ctDo.get(i).country_name);
			cv.put("LAT", ctDo.get(i).lat);
			cv.put("LANG", ctDo.get(i).lang);

			db.insert(CITYTABLE, null, cv);
		}
		closeDB();
	}

	public int LocationTableEmpty() {
		int count = 0;

		openDB();
		Cursor cr = db.rawQuery("Delete  from " + LOCATIONS, null);
		count = cr.getCount();
		closeDB();
		return count;

	}

	public int CountryTableEmpty() {
		int count = 0;

		openDB();
		Cursor cr = db.rawQuery("Delete  from " + CITYTABLE, null);
		count = cr.getCount();
		closeDB();
		return count;

	}

	public int isTableExist() {
		int count = 0;

		openDB();
		Cursor cr = db.rawQuery("select * from " + LOCATIONS, null);
		count = cr.getCount();
		closeDB();
		return count;

	}

}
