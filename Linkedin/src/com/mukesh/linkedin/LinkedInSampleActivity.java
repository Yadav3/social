package com.mukesh.linkedin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.linkedin.R;
import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.google.code.linkedinapi.schema.Person;
import com.mukesh.linkedin.LinkedinDialog.OnVerifyListener;

/**
 * Mukesh Kumar Yadav
 **/
public class LinkedInSampleActivity extends Activity {
	Button login;
	Button share;
	EditText et;
	TextView name;
	ImageView photo;
	public static final String OAUTH_CALLBACK_HOST = "litestcalback";

	final LinkedInOAuthService oAuthService = LinkedInOAuthServiceFactory
            .getInstance().createLinkedInOAuthService(
                    Config.LINKEDIN_CONSUMER_KEY,Config.LINKEDIN_CONSUMER_SECRET, Config.scopeParams);
	final LinkedInApiClientFactory factory = LinkedInApiClientFactory
			.newInstance(Config.LINKEDIN_CONSUMER_KEY,
					Config.LINKEDIN_CONSUMER_SECRET);
	LinkedInRequestToken liToken;
	LinkedInApiClient client;
	LinkedInAccessToken accessToken = null;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	 	
		if( Build.VERSION.SDK_INT >= 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy); 
		}
		share = (Button) findViewById(R.id.share);
		name = (TextView) findViewById(R.id.name);
		et = (EditText) findViewById(R.id.et_share);
		login = (Button) findViewById(R.id.login);
		photo = (ImageView) findViewById(R.id.photo);

		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				linkedInLogin();
			}
		});

		// share on linkedin
		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String share = et.getText().toString();
				if (null != share && !share.equalsIgnoreCase("")) {
					OAuthConsumer consumer = new CommonsHttpOAuthConsumer(Config.LINKEDIN_CONSUMER_KEY, Config.LINKEDIN_CONSUMER_SECRET);
	 			    consumer.setTokenWithSecret(accessToken.getToken(), accessToken.getTokenSecret());
					DefaultHttpClient httpclient = new DefaultHttpClient();
					HttpPost post = new HttpPost("https://api.linkedin.com/v1/people/~/shares");
					try {
						consumer.sign(post);
					} catch (OAuthMessageSignerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OAuthExpectationFailedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OAuthCommunicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // here need the consumer for sign in for post the share
				 	post.setHeader("content-type", "text/XML");
					String myEntity = "<share><comment>"+ share +"</comment><visibility><code>anyone</code></visibility></share>";
					/*{
						  "comment": "Check out developer.linkedin.com! http://linkd.in/1FC2PyG",
						  "visibility": {
						    "code": "anyone"
						  }
						}*/
					
//				String myEntity="<share><comment>"+share+"</comment><content><title>LinkedIn Developer Resources</title><description>Leverage LinkedIn's APIs to maximize engagement</description>" +
//						"<submitted-url>https://developer.linkedin.com</submitted-url><submitted-image-url>https://example.com/logo.png</submitted-image-url></content>" +
//						"<visibility><code>anyone</code></visibility></share>";
					
					try {
						post.setEntity(new StringEntity(myEntity));
		 				@SuppressWarnings("unused")
						org.apache.http.HttpResponse response = httpclient.execute(post);
						Toast.makeText(LinkedInSampleActivity.this,
								"Shared sucessfully", Toast.LENGTH_SHORT).show();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					Toast.makeText(LinkedInSampleActivity.this,
							"Please enter the text to share",
							Toast.LENGTH_SHORT).show();
				}
				
				/*String share = et.getText().toString();
				if (null != share && !share.equalsIgnoreCase("")) {
					client = factory.createLinkedInApiClient(accessToken);
					client.postNetworkUpdate(share);
					et.setText("");
					Toast.makeText(LinkedInSampleActivity.this,
							"Shared sucessfully", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(LinkedInSampleActivity.this,
							"Please enter the text to share",
							Toast.LENGTH_SHORT).show();
				}*/
			}
		});
	}

	private void linkedInLogin() {
		ProgressDialog progressDialog = new ProgressDialog(
				LinkedInSampleActivity.this);

		LinkedinDialog d = new LinkedinDialog(LinkedInSampleActivity.this,
				progressDialog);
		d.show();

		// set call back listener to get oauth_verifier value
		d.setVerifierListener(new OnVerifyListener() {
			@Override
			public void onVerify(String verifier) {
				try {
					Log.i("LinkedinSample", "verifier: " + verifier);

		 			accessToken = LinkedinDialog.oAuthService
							.getOAuthAccessToken(LinkedinDialog.liToken,
									verifier);
					LinkedinDialog.factory.createLinkedInApiClient(accessToken);
					client = factory.createLinkedInApiClient(accessToken);
					// client.postNetworkUpdate("Testing by Mukesh!!! LinkedIn wall post from Android app");
					Log.i("LinkedinSample",
							"ln_access_token: " + accessToken.getToken());
					
					Person p = client.getProfileForCurrentUser(); 
					Log.i("LinkedinSampleId",
							"getId: " + p.getId()); 
					name.setText("Welcome " + p.getFirstName() + " "
							+ p.getLastName()+ " "+p.getId());
					
					/* GroupMemberships member=client.getGroupMemberships();
					 for(com.google.code.linkedinapi.schema.GroupMembership p1 :member.getGroupMembershipList()) {
					 Log.e("Name",""+p1.getPerson());
					 Log.e("Name",""+p1.getContactEmail());
					 Log.e("Name",""+p1.getGroup());
					 //Log.e("Description",""+p.getDescription());
					 Log.e("      ",""+"*****************");
					 }*/
					 
					/*
					///////////////////////////////////////
					 Person profile = client.getProfileById(p.getId());
					Log.e("Name:",
					"" + profile.getFirstName() + " " + profile.getLastName());
					Log.e("BirthDate:", "" + profile.getDateOfBirth());
					Log.e("Headline:", "" + profile.getHeadline());
					Log.e("Summary:", "" + profile.getSummary());
					Log.e("Industry:", "" + profile.getIndustry());
					Log.e("Picture:", "" + profile.getPictureUrl());
					Log.e("Picture:", "" + profile.getApiStandardProfileRequest() + "\n"
					+ profile.getSiteStandardProfileRequest().getUrl());
					tv.setText(profile.getLastName() + " " + profile.getFirstName());

					Groups categoryImpl = client.getSuggestedGroups();
					for (Group p : categoryImpl.getGroupList()) {
					Log.e("Name", "" + p.getName());
					// Log.e("Description",""+p.getDescription());
					Log.e("      ", "" + "*****************");
					}

					Companies companies = client.getFollowedCompanies();
					for (Company p1 : companies.getCompanyList()) {
					Log.e("Comny Name", "" + p1.getName());
					// Log.e("Description",""+p.getDescription());
					Log.e("      ", "" + "*****************");
					}
 
					Connections connections = client.getConnectionsForCurrentUser();
					for (Person p1 : connections.getPersonList()) {
					Log.e("Name", "" + p1.getLastName() + " " + p1.getFirstName());
					Log.e("Industry      ", "" + p1.getIndustry());
					Log.e("      ", "" + "*****************");
					// Log.e("currentStatus ",""+p.getCurrentStatus());
					// Log.e("link          ",""+p.getPublicProfileUrl());
					// Log.e("position      ",""+p.getEducations());
					}
					///////////////////////////////////////
					*/
					
					name.setVisibility(0);
					login.setVisibility(4);
					share.setVisibility(0);
					et.setVisibility(0);

				} catch (Exception e) {
					Log.i("LinkedinSample", "error to get verifier");
					e.printStackTrace();
				}
			}
		});

		// set progress dialog
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(true);
		progressDialog.show();
	}
}

