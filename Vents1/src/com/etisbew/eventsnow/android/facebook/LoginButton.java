package com.etisbew.eventsnow.android.facebook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.etisbew.eventsnow.android.facebook.Facebook.DialogListener;
import com.etisbew.eventsnow.android.facebook.SessionEvents.AuthListener;
import com.etisbew.eventsnow.android.facebook.SessionEvents.LogoutListener;


public class LoginButton extends Button {

	private Facebook mFb;
	private Handler mHandler;
	private SessionListener mSessionListener = new SessionListener();
	private String[] mPermissions;
	private Activity mActivity;

	public LoginButton(Context context) {
		super(context);
	}

	public LoginButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LoginButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void init(final Activity activity, final Facebook fb) {
		init(activity, fb, new String[] { "read_stream", "email" });
	}

	public void init(final Activity activity, final Facebook fb,
			final String[] permissions) {
		mActivity = activity;
		mFb = fb;
		mPermissions = permissions;
		mHandler = new Handler();

//		setBackgroundColor(Color.TRANSPARENT);
//		setAdjustViewBounds(true);
//		this.setText("Login with FB");
//		this.setBackgroundColor(Color.BLUE);
//		setImageResource(R.drawable.fb_button);
		
		
		drawableStateChanged();

		SessionEvents.addAuthListener(mSessionListener);
		SessionEvents.addLogoutListener(mSessionListener);
		setOnClickListener(new ButtonOnClickListener());
	}

	private final class ButtonOnClickListener implements OnClickListener {

		public void onClick(View arg0) {
			if (mFb.isSessionValid()) {
				SessionEvents.onLogoutBegin();
				AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFb);
				asyncRunner.logout(getContext(), new LogoutRequestListener());
//				new FbLoginTask(mActivity).execute();
				
			} else {
				mFb.authorize(mActivity, mPermissions,
						new LoginDialogListener());
			}
		}
	}

	private final class LoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			SessionEvents.onLoginSuccess();
		}

		public void onFacebookError(FacebookError error) {
			SessionEvents.onLoginError(error.getMessage());
		}

		public void onError(DialogError error) {
			SessionEvents.onLoginError(error.getMessage());
		}

		public void onCancel() {
			SessionEvents.onLoginError("Action Canceled");
		}
	}

	private class LogoutRequestListener extends BaseRequestListener {
		public void onComplete(String response) {
			// callback should be run in the original thread,
			// not the background thread
			mHandler.post(new Runnable() {
				public void run() {
					Util.clearCookies(mActivity);
					SessionEvents.onLogoutFinish();
				}
			});
		}
	}

	private class SessionListener implements AuthListener, LogoutListener {

		public void onAuthSucceed() {
//			setImageResource(R.drawable.fb);
			SessionStore.save(mFb, getContext());
		}

		public void onAuthFail(String error) {
		}

		public void onLogoutBegin() {
		}

		public void onLogoutFinish() {
			Util.clearCookies(getContext());
			SessionStore.clear(getContext());
//			setImageResource(R.drawable.fb);
		}
	}

}
