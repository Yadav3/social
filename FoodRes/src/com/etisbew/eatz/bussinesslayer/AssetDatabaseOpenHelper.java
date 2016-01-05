package com.etisbew.eatz.bussinesslayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.etisbew.eatz.objects.LocationDO;

public class AssetDatabaseOpenHelper {
 
    private static final String DB_NAME = "EATZ.db";
 
    private Context context;
    private SQLiteDatabase sqliteDB = null; 
    String DB_PATH;
 
    public AssetDatabaseOpenHelper(Context context) {
        this.context = context;
        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
    }
 
    public SQLiteDatabase openDatabase() {
        File dbFile = context.getDatabasePath(DB_PATH + DB_NAME);
 
        if (!dbFile.exists()) {
            try {
                copyDatabase(dbFile);
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }
 
        sqliteDB  = SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
        return sqliteDB;
    }
 
    private void copyDatabase(File dbFile) throws IOException {
        InputStream is = context.getAssets().open(DB_NAME);
        OutputStream os = new FileOutputStream(dbFile);
 
        byte[] buffer = new byte[1024];
        while (is.read(buffer) > 0) {
            os.write(buffer);
        }
 
        os.flush();
        os.close();
        is.close();
    }
    
    public void closeDB(){
    	if(sqliteDB !=null){
    		sqliteDB.close();
    	}
    }
    
    public final String KEYWORDSTABLE = "KeywordsTable";
	public final String LOCATIONS = "LocationTable";
    
    public ArrayList<LocationDO> getAllLocations(){
		
    	openDatabase();
		ArrayList<LocationDO> ldLocation = new ArrayList<LocationDO>();
		
		Cursor cr = sqliteDB.rawQuery("select * from "+LOCATIONS, null);
		if(cr.getCount()>0){
			while (cr.moveToNext()) {
				LocationDO ld = new LocationDO();
				ld.id = cr.getString(cr.getColumnIndex("ID"));
				ld.name = cr.getString(cr.getColumnIndex("NAME"));
				ld.cat = "loc";
				ldLocation.add(ld);
			}
		}
		
		closeDB();
		
		return ldLocation;
		
	}
    
    
    public ArrayList<LocationDO> getAllKeys(){
		
    	openDatabase();
		ArrayList<LocationDO> ldKeys = new ArrayList<LocationDO>();
		
		Cursor cr = sqliteDB.rawQuery("select * from "+KEYWORDSTABLE, null);
		if(cr.getCount()>0){
			while (cr.moveToNext()) {
				LocationDO ld = new LocationDO();
				ld.id = cr.getString(cr.getColumnIndex("ID"));
				ld.name = cr.getString(cr.getColumnIndex("NAME"));
				ld.cat = "key";
				
				ldKeys.add(ld);
			}
		}
		
		closeDB();
		
		return ldKeys;
		
	}
    
    
    
 
}