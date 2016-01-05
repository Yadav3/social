package com.etisbew.eatz.android;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashActivity extends BaseActivity{
	RelativeLayout user_enable;
	TextView user_clicked;
	/*SharedPreferences pref;
	Editor editor;
	public static String prefName = "Random";
	boolean LanguageFlag = true;*/
	
	 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub 
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		/*SharedPreferences settings = getSharedPreferences("prefs", 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean("firstRun", true);
	    editor.commit();

	    Intent intent = new Intent(this, Localsecrets.class);
	    startActivity(intent);*/
	    
	 
/*		
//		String name = pref.getString(
//				prefName, "");
//		 if (prefs.getString("FirstTime", true) == true){
		 pref = getSharedPreferences(prefName, MODE_PRIVATE);
		 
		 
//		pref.getDefaultSharedPreferences(prefName).edit().putString("MYLABEL", "myStringToSave").commit();
		if (pref.getBoolean("FirstTime", true) == true){

			
			startActivityForResult(new Intent(
					getApplicationContext(), SplashActivity.class), 0);

		} else {
		//	PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound"); 
			editor = pref.edit();
			editor.putBoolean("FirstTime", false);
			editor.commit();
			LanguageFlag = true;
			startActivityForResult(new Intent(
					getApplicationContext(), Localsecrets.class),
					0);

		}*/
 
		
		setContentView(R.layout.splash_screen);
		
		
		user_enable=(RelativeLayout) findViewById(R.id.user_enable);
		user_clicked = (TextView) findViewById(R.id.user_clicked);
		user_enable.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Navigation();
			}
		});
		user_clicked.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Navigation();
			}
		}); 
	}
	public void Navigation(){
		startActivity(new Intent(SplashActivity.this,Localsecrets.class));
		finish();
	}
	@Override
    public void onBackPressed() {
        super.onBackPressed(); 
      
    }
 @Override
	public void onResume(){
	    super.onResume();
	    
	    
	
	}
}
