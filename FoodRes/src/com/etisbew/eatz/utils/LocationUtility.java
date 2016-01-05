package com.etisbew.eatz.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.etisbew.eatz.common.Appconstants;
import com.etisbew.eatz.objects.LocationDO;

public class LocationUtility 
{
	private static Context mContext;
    private static LocationUtility _this;
    private static LocationManager locationManager;
	private LocationResultListener locationResultListener;
	@SuppressWarnings("unused")
	private LocationAddressListener locationAdressListener;
	private boolean gps_enabled = false, network_enabled = false;
	private Handler mHandler = new Handler();
	private GetLastLocationRunnable lastLocationRunnable;
	
	private static final int DELAY_TIME = 3*1000;
	private static final int MAX_DELAY_TIME = 120*1000;
	
    private LocationUtility()
    {
    	
    }
    
    public static LocationUtility getInstance(Context context)
    {
    	mContext = context;
    	
    	if(_this == null)
    		_this = new LocationUtility();
    	
    	if(locationManager == null && mContext != null)
        	locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    	
    	return _this;
    }
    
    public boolean isLocationProviderEnabled()
    {
		//exceptions will be thrown if provider is not permitted.
        try
        {
        	gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        	network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch(Exception ex)
        {
        	Log.i("LocationUtility", "Error while checking provider enable or not:"+ex.toString());
        }
        
    	return gps_enabled || network_enabled;
    }
    
    public void setLocationResultListener(LocationResultListener locationResultListener)
    {
    	this.locationResultListener = locationResultListener;
    }
    public void setLocationAddrssListener(LocationAddressListener locationAddressListener)
    {
    	this.locationAdressListener = locationAddressListener;
    }
    
   
    
    
    public void startFetchingLocation()
    {
    	if(isLocationProviderEnabled())
    	{
    		if(gps_enabled)
        	{
    			Log.i("LocationUtility", "LocationUtility GPS_PROVIDER enabled");
            	
            	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        	}
        	
        	if(network_enabled)
            {
        		Log.i("LocationUtility", "LocationUtility NETWORK_PROVIDER enabled");
            	
            	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }
    	}
    }
    
    public void startFetchingLocation(boolean waitMaxTimeForLocation)
    {
    	lastLocationRunnable = new GetLastLocationRunnable(waitMaxTimeForLocation);
    	
    	if(waitMaxTimeForLocation)
    		mHandler.postDelayed(lastLocationRunnable, MAX_DELAY_TIME);   
    	else
    		mHandler.postDelayed(lastLocationRunnable, DELAY_TIME);    		
    	
    	startFetchingLocation();
    }
    
    class GetLastLocationRunnable implements Runnable
	{
    	boolean waitMaxTimeForLocation;
    	
    	GetLastLocationRunnable(boolean waitMaxTimeForLocation)
    	{
    		this.waitMaxTimeForLocation = waitMaxTimeForLocation;
    	}
    	
		@Override
		public void run() 
		{
			if(waitMaxTimeForLocation)
	    	{
				stopGpsLocUpdation();
	    	}
			
			getLocation();
		}
	}
    
    private void getLocation()
    {
    	Location net_loc = null, gps_loc = null;
        
    	if(gps_enabled)
    		gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        
    	if(network_enabled)
    		net_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
         
    	if(gps_loc != null && net_loc != null)
    	{
    		if(gps_loc.getTime() > net_loc.getTime())
    			updateLocation(gps_loc);
    		else
    			updateLocation(net_loc);
    	}
    	else if(gps_loc != null)
    		updateLocation(gps_loc);
    	else if(net_loc != null)
    		updateLocation(net_loc);
    	else 
    		updateLocation(null);
    }
     
    LocationListener locationListener = new LocationListener() 
    {
        @Override
		public void onLocationChanged(Location loc)
        {
        	mHandler.removeCallbacks(lastLocationRunnable);
        	
        	stopGpsLocUpdation();
        	
        	updateLocation(loc);
        }
        @Override
		public void onProviderDisabled(String provider) {}
        @Override
		public void onProviderEnabled(String provider) {}
        @Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}
    };
	
    public interface LocationResultListener
    {
    	public void gotLocation(Location loc);
    }
    
    public interface LocationAddressListener
    {
    	public void gotAddress(LocationDO lod);
    }
    
    
    
    public void stopGpsLocUpdation() 
    {
    	if(locationManager != null)
    	{
    		locationManager.removeUpdates(locationListener);
    	}
    }
    
    private void updateLocation(Location location)
    {
    	Log.i("LocationUtility", "LocationUtility updateLocation()");
    	
    	if(location != null)
    	{
    		Appconstants.LATTITUDE = String.valueOf(location.getLatitude());
            Appconstants.LANGITUDE = String.valueOf(location.getLongitude());
            
    	}
    	else
    	{
    		Appconstants.LATTITUDE = "0";
    		Appconstants.LANGITUDE = "0";
    	}
    	Log.i("LocationUtility", "LocationUtility AppConstants.latitude="+Appconstants.LATTITUDE+", AppConstants.longitude="+Appconstants.LANGITUDE);
    	
    	if(locationResultListener != null)
    		locationResultListener.gotLocation(location);
    	
    }
    
}