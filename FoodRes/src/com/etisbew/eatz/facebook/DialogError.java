package com.etisbew.eatz.facebook;

/**
 * Encapsulation of Dialog Error.
 *
 * @author ssoneff@facebook.com
 */
public class DialogError extends Throwable {

    private static final long serialVersionUID = 1L;

    /**
     * The ErrorCode received by the WebView: see
     * http://developer.android.com/reference/android/webkit/WebViewClient.html
     */
    private int mErrorCode;

    /** The URL that the dialog was trying to load */
    private String mFailingUrl;

    public DialogError(String message, int errorCode, String failingUrl) {
        super(message);
        mErrorCode = errorCode;
        mFailingUrl = failingUrl;
    }

    int getErrorCode() {
        return mErrorCode;
    }

    String getFailingUrl() {
        return mFailingUrl;
    }

}
