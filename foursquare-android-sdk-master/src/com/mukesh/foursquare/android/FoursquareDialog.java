package com.mukesh.foursquare.android;

/**
 * Encapsulation of Foursquare Dialog.
 *
 * @author Mukesh Yadav
 */
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiramot.foursquare.android.R;
import com.mukesh.foursquare.android.Foursquare.DialogListener;

public class FoursquareDialog extends Dialog {

	static final int FB_BLUE = 0xFF0cbadf;
	static final float[] DIMENSIONS_LANDSCAPE = { 460, 260 };
	static final float[] DIMENSIONS_PORTRAIT = { 280, 420 };
	@SuppressWarnings("deprecation")
	static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.FILL_PARENT,
			ViewGroup.LayoutParams.FILL_PARENT);
	static final int MARGIN = 4;
	static final int PADDING = 2;
	static final String DISPLAY_STRING = "touch";
	static final String FB_ICON = "icon.png";

	private String mUrl;
	private DialogListener mListener;
	private ProgressDialog mSpinner;
	private WebView mWebView;
	private LinearLayout mContent;
	private TextView mTitle;

	public FoursquareDialog(Context context, String url, DialogListener listener) {
		super(context);
		mUrl = url;
		mListener = listener;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSpinner = new ProgressDialog(getContext());
		mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mSpinner.setMessage("Loading...");

		mContent = new LinearLayout(getContext());
		mContent.setOrientation(LinearLayout.VERTICAL);
		setUpTitle();
		setUpWebView();
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		final float scale = getContext().getResources().getDisplayMetrics().density;
		float[] dimensions = (display.getWidth() < display.getHeight()) ? DIMENSIONS_PORTRAIT
				: DIMENSIONS_LANDSCAPE;
		addContentView(mContent, new FrameLayout.LayoutParams(
				(int) (dimensions[0] * scale + 0.5f), (int) (dimensions[1]
						* scale + 0.5f)));
	} 

	private void setUpTitle() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Drawable icon = getContext().getResources().getDrawable(
				R.drawable.foursquare);
		mTitle = new TextView(getContext());
		mTitle.setText("Foursquare");
		mTitle.setTextColor(Color.WHITE);
		mTitle.setTypeface(Typeface.DEFAULT_BOLD);
		mTitle.setBackgroundColor(FB_BLUE);
		mTitle.setPadding(MARGIN + PADDING, MARGIN, MARGIN, MARGIN);
		mTitle.setCompoundDrawablePadding(MARGIN + PADDING);
		mTitle.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
		mContent.addView(mTitle);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setUpWebView() {
		mWebView = new WebView(getContext());
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setWebViewClient(new FoursquareDialog.FoursquareWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(mUrl);
		mWebView.setLayoutParams(FILL);
		mContent.addView(mWebView);
	}
 
	private class FoursquareWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d("Foursquare-WebView", "Redirect URL: " + url);
			if (url.startsWith(Foursquare.REDIRECT_URI)) {
				Bundle values = Util.parseUrl(url);
				mListener.onComplete(values);
				// String error = values.getString("error");
				// if (error == null) {
				// error = values.getString("error_type");
				// }
				//
				// if (error == null) {
				// mListener.onComplete(values);
				// } else if (error.equals("access_denied") ||
				// error.equals("OAuthAccessDeniedException")) {
				// mListener.onCancel();
				// } else {
				// mListener.onFoursquareError(new FoursquareError(error));
				// }

				FoursquareDialog.this.dismiss();
				return true;
			} else if (url.contains(DISPLAY_STRING)) {
				return false;
			}
			
			getContext().startActivity(
					new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			return true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			mListener.onError(new DialogError(description, errorCode,
					failingUrl));
			FoursquareDialog.this.dismiss();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d("Foursquare-WebView", "Webview loading URL: " + url);
			if (checkRedirectUrl(url)) {
				FoursquareDialog.this.dismiss();
			} else {
				super.onPageStarted(view, url, favicon);
				mSpinner.show();
			}
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			String title = mWebView.getTitle();
			if (title != null && title.length() > 0) {
				mTitle.setText(title);
			}
			mSpinner.dismiss();

		}

		private boolean checkRedirectUrl(String url) {
			if (url.startsWith(Foursquare.REDIRECT_URI)) {
				Bundle values = Util.parseUrl(url);
				mListener.onComplete(values);

				FoursquareDialog.this.dismiss();
				return true;
			}
			return false;
		}

	}
}
