package com.mukesh.linkedin;

public class Config {

	public static String LINKEDIN_CONSUMER_KEY = "755kn7fzmf7qyb";
	public static String LINKEDIN_CONSUMER_SECRET = "13JvEKXeA7s8oqrL";
	public static String scopeParams = "rw_nus+r_emailaddress+r_basicprofile";
//	params.set("scope", "r_emailaddress r_basicprofile");
	
	public static String OAUTH_CALLBACK_SCHEME = "x-oauthflow-linkedin";
	public static String OAUTH_CALLBACK_HOST = "callback";
	public static String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;
}
