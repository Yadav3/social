package com.etisbew.eventsnow.android.database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EventsNowDatabaseConnection {
	private static final String DATABASE_NAME = "EventsNow.sqlite";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase dbWrite;
    private String mDatabasePath;
    private DBHelper mDatabaseHelper;
    private Context mContext;
    SQLiteDatabase checkDB = null;
   
    public EventsNowDatabaseConnection (Context context){
            mContext = context;
            mDatabaseHelper = new DBHelper(context);
            mDatabasePath = context.getDatabasePath(DATABASE_NAME).toString();
            System.out.println("mDatabasePath"+mDatabasePath);
            try {
                    createDataBase();
            } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }
    }
    public void createDataBase() throws IOException {
           
        if (!checkDataBase()) {
            mDatabaseHelper.getReadableDatabase();
            this.dbWrite = mDatabaseHelper.getWritableDatabase();
            try {
                    copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
            boolean check=false;
        
        try {
            checkDB = SQLiteDatabase.openDatabase(mDatabasePath, null,
                    SQLiteDatabase.OPEN_READWRITE);
            

        } catch (SQLiteException e) {
            Log.v("DB", "No DB");
            // database does't exist yet.
        }
        if (check = (checkDB != null)) {
            checkDB.close();
        }
        return check;
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException {

        // Open your local db as the input stream
        InputStream myInput = (InputStream) mContext.getResources().getAssets().open(DATABASE_NAME);

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(mDatabasePath);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

   public SQLiteDatabase getDB(){
	  // checkDB.close();
	   try{
		   return mDatabaseHelper.getWritableDatabase();
	   }catch(Exception e){
		 e.printStackTrace(); 
		 return mDatabaseHelper.getWritableDatabase();
	   }
	
   }

    public String getDatabasePath(){
            return mDatabasePath;
    }      
   
    public SQLiteDatabase open (){
    	//checkDB.openDatabase(mDatabasePath, null, SQLiteDatabase.OPEN_READWRITE);
            return mDatabaseHelper.getReadableDatabase();
    }
    public void close(){
    	checkDB.close();
    		mDatabaseHelper.close(); 
    }
    public void dbClose(){
    	mDatabaseHelper.close(); 
    }
   
    private static class DBHelper extends SQLiteOpenHelper {
           
            public DBHelper(Context context) {
                    super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }

            // Method is called during creation of the database
            public void onCreate(SQLiteDatabase database) {              
            }
           
            // Method is called during an upgrade of the database,
            // e.g. if you increase the database version
            public void onUpgrade(SQLiteDatabase database, int oldVersion,
                    int newVersion) {                    
            }
    }

}
