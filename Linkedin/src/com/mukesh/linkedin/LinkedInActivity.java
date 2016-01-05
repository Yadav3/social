package com.mukesh.linkedin;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.linkedin.R;
import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.google.code.linkedinapi.schema.Companies;
import com.google.code.linkedinapi.schema.Company;
import com.google.code.linkedinapi.schema.Connections;
import com.google.code.linkedinapi.schema.Group;
import com.google.code.linkedinapi.schema.Groups;
import com.google.code.linkedinapi.schema.Person;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class LinkedInActivity extends Activity implements OnClickListener {

	public static final String CONSUMER_KEY = "755kn7fzmf7qyb";
	public static final String CONSUMER_SECRET = "13JvEKXeA7s8oqrL";

	public static final String APP_NAME = "YOR APP NAME";
	public static final String OAUTH_CALLBACK_SCHEME = "x-oauthflow-linkedin";
	public static final String OAUTH_CALLBACK_HOST = "litestcalback";
	public static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME
			+ "://" + OAUTH_CALLBACK_HOST;

	final LinkedInOAuthService oAuthService = LinkedInOAuthServiceFactory
			.getInstance().createLinkedInOAuthService(CONSUMER_KEY,
					CONSUMER_SECRET);
	final LinkedInApiClientFactory factory = LinkedInApiClientFactory
			.newInstance(CONSUMER_KEY, CONSUMER_SECRET);
	LinkedInRequestToken liToken;
	LinkedInApiClient client;

	TextView tv = null;
	Button signIn, btn_status, post;
	EditText currentStatus;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mains);
		if( Build.VERSION.SDK_INT >= 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy); 
		}
		tv = (TextView) findViewById(R.id.tv);
		signIn = (Button) findViewById(R.id.btn_sign);
		btn_status = (Button) findViewById(R.id.btn_post);
		post = (Button) findViewById(R.id.btn_send);
		currentStatus = (EditText) findViewById(R.id.edt_post);

		signIn.setOnClickListener(this);
		btn_status.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_sign:
			liToken = oAuthService.getOAuthRequestToken(OAUTH_CALLBACK_URL);
			Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(liToken
					.getAuthorizationUrl()));
			startActivity(i);
			break;
		case R.id.btn_post:
			post.setVisibility(View.VISIBLE);
			currentStatus.setVisibility(View.VISIBLE);
			break;
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {

		btn_status.setVisibility(View.VISIBLE);

		String verifier = intent.getData().getQueryParameter("oauth_verifier");

		LinkedInAccessToken accessToken = oAuthService.getOAuthAccessToken(
				liToken, verifier);
		client = factory.createLinkedInApiClient(accessToken);
		client.postNetworkUpdate("LinkedIn Android app test");
		Person profile = client.getProfileForCurrentUser();

		post.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String status = currentStatus.getText().toString();
				client.updateCurrentStatus(status);
				currentStatus.setText("");
			}
		});

		// for update status......
		//

		// Person profile = client.getProfileById(id);
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

		// GroupMemberships member=client.getGroupMemberships();
		// for(GroupMembership p :member.getGroupMembershipList()) {
		// Log.e("Name",""+p.getPerson());
		// Log.e("Name",""+p.getContactEmail());
		// Log.e("Name",""+p.getGroup());
		// //Log.e("Description",""+p.getDescription());
		// Log.e("      ",""+"*****************");
		// }

		Companies companies = client.getFollowedCompanies();
		for (Company p : companies.getCompanyList()) {
			Log.e("Comny Name", "" + p.getName());
			// Log.e("Description",""+p.getDescription());
			Log.e("      ", "" + "*****************");
		}

		Connections connections = client.getConnectionsForCurrentUser();
		for (Person p : connections.getPersonList()) {
			Log.e("Name", "" + p.getLastName() + " " + p.getFirstName());
			Log.e("Industry      ", "" + p.getIndustry());
			Log.e("      ", "" + "*****************");
			// Log.e("currentStatus ",""+p.getCurrentStatus());
			// Log.e("link          ",""+p.getPublicProfileUrl());
			// Log.e("position      ",""+p.getEducations());
		}

	}

}
