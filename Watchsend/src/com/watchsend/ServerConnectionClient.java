package com.watchsend;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * A Server Connection client that is usually not 
 * directly used by the programmer, but is used in the form
 * of a ServerAPI call. 
 * 
 * Basically implements a couple main HTTP functions,
 * POST and GET.
 * 
 */

public class ServerConnectionClient {
	    /* The URL to post to. */
        private static final String BASE_URL = "http://api.watchsend.com/";

        private static AsyncHttpClient client = new AsyncHttpClient();

        public static void get(String url, RequestParams params,
                        AsyncHttpResponseHandler responseHandler) {
                client.get(getAbsoluteUrl(url), params, responseHandler);
        }

        public static void post(String url, RequestParams params,
                        AsyncHttpResponseHandler responseHandler) {
                client.post(getAbsoluteUrl(url), params, responseHandler);
        }

        private static String getAbsoluteUrl(String relativeUrl) {
                return BASE_URL + relativeUrl;
        }

}
