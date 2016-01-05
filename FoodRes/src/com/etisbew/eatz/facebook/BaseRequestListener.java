package com.etisbew.eatz.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import com.etisbew.eatz.facebook.AsyncFacebookRunner.RequestListener;

/**
 * Skeleton base class for RequestListeners, providing default error handling.
 * Applications should handle these error conditions.
 * 
 */
public abstract class BaseRequestListener implements RequestListener {

	@Override
	public void onFacebookError(FacebookError e) {
		e.printStackTrace();
	}

	@Override
	public void onFileNotFoundException(FileNotFoundException e) {
		e.printStackTrace();
	}

	@Override
	public void onIOException(IOException e) {
		e.printStackTrace();
	}

	@Override
	public void onMalformedURLException(MalformedURLException e) {
		e.printStackTrace();
	}

}
