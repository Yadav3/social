package com.etisbew.eventsnow.android.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Skeleton base class for RequestListeners, providing default error handling.
 * Applications should handle these error conditions.
 * 
 */
public abstract class BaseRequestListener implements com.etisbew.eventsnow.android.facebook.AsyncFacebookRunner.RequestListener {

	public void onFacebookError(FacebookError e) {
		e.printStackTrace();
	}

	public void onFileNotFoundException(FileNotFoundException e) {
		e.printStackTrace();
	}

	public void onIOException(IOException e) {
		e.printStackTrace();
	}

	public void onMalformedURLException(MalformedURLException e) {
		e.printStackTrace();
	}

}
