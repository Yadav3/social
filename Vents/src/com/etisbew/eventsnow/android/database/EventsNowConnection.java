package com.etisbew.eventsnow.android.database;

import java.util.ArrayList;
import java.util.List;

import com.etisbew.eventsnow.android.beans.StatesDetails;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class EventsNowConnection {

	EventsNowDatabaseConnection dbConnection;
	SQLiteDatabase db;
	StringBuffer query = new StringBuffer();	
	
	public EventsNowConnection(Context context){
		dbConnection  = new EventsNowDatabaseConnection(context);
		this.db = dbConnection.getDB();	
	}
	public void close(){
		dbConnection.dbClose();
	}
	public String getDBPath(){
		return dbConnection.getDatabasePath();
	}

	public List<StatesDetails> getAllItemDetails() {
		List<StatesDetails> todos = new ArrayList<StatesDetails>();
		Cursor c = null;
		 query.setLength(0);
		 query.append("SELECT id,state_id,state_name,country_id from States ");

		Log.e("query", ""+query);

		 c = db.rawQuery(query.toString(), null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				StatesDetails id = new StatesDetails();
				id.setId(c.getInt((c.getColumnIndex("id"))));
				id.setState_id(c.getInt(c.getColumnIndex("state_id")));
				id.setState_name(c.getString(c.getColumnIndex("state_name")));
				id.setCountry_id(c.getInt(c.getColumnIndex("country_id")));

				// adding to todo list
				todos.add(id);
			} while (c.moveToNext());
		}

		return todos;
	}

	/*private int getStateId(String state_name){
		 Cursor cursorstateId = null;
		 int state_id = 0;
		 query.setLength(0);
		 query.append("SELECT  state_id as state_id FROM States where state_name='"+state_name+"'");
		 cursorstateId = db.rawQuery(query.toString(), null);
		 try{
			if (cursorstateId.moveToFirst()) {
		        do {
		        	
		        	state_id = cursorstateId.getInt(cursorstateId.getColumnIndex("state_id"));
		        				        	
		        } while (cursorstateId.moveToNext());
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		      try{
		             if (cursorstateId!=null){
		            	 cursorstateId.close();
		             }		             
		         }catch (Exception e){
		            e.printStackTrace();
		         }
			  }
		 return state_id;
		 
	}*/
}
