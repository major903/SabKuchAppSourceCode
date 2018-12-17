package vedam.subkuch.network;

import android.content.Context;
import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.crashlytics.android.Crashlytics;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import vedam.subkuch.BuildConfig;
import vedam.subkuch.SubKuchApplication;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.utils.AppPrefs;

/**
 * @author gour
 */
public class HelperVolley<T> {

    /**
     * Default charset for JSON request.
     */
    private static final String PROTOCOL_CHARSET = "utf-8";

    /**
     * Content type for request.
     */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private HelperVolley() {
    }

    public static void cancelRequest(Context context, String tag) {

        RequestQueue mRequestQueue = ((SubKuchApplication) context.getApplicationContext()).getRequestQueue();
        mRequestQueue.cancelAll(tag);
    }

    public static String buildURIWithQueryParameters(String apiServiceName, HashMap<String, String> params) {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(NetworkConstants.API_SCHEME)
                .authority(NetworkConstants.HOST_NAME)
                .appendPath("api")
                .appendEncodedPath(apiServiceName);


        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> e : params.entrySet()) {
                String key = e.getKey();
                String value = e.getValue();

                builder.appendQueryParameter(key, value);

            }
        }
        return builder.build().toString();
    }


    static <T> void callApiWithJson(Context context, String url, String API, Listener<T> listener,
                                    final String json, Type repClass, Response.ErrorListener errorListener, final HashMap<String, String> params) {
        callApiWithJson(context, url, API, listener, json, repClass, errorListener, params, Request.Method.POST);
    }

    private static <T> void callApiWithJson(Context context, String url, String API, Listener<T> listener,
                                            final String json, Type repClass, Response.ErrorListener errorListener, final HashMap<String, String> params, int method) {
        if (BuildConfig.DEBUG) {
            System.out.println("url " + url);
            System.out.println("params " + params);
        }

        GsonRequest<T> myReq = new GsonRequest<T>(
                method, url,
                repClass, null, listener, errorListener) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;UTF-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                if (params != null && params.size() > 0) {
                    return encodeParameters(params, getParamsEncoding());
                }
                return null;
            }

            @Override
            protected Response<T> parseNetworkResponse(NetworkResponse response) {
                try {
                    if (response.data.length == 0) {
                        byte[] responseData = "{}".getBytes("UTF-8");
                        response = new NetworkResponse(response.statusCode, responseData, response.headers, response.notModified);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                if (headers == null || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<>();
                }

                return headers;
            }
        };

        RequestQueue mRequestQueue = ((SubKuchApplication) context.getApplicationContext()).getRequestQueue();
        myReq.setTag(API);
        mRequestQueue.add(myReq);

    }

    static <T> void callApiWithBody(Context context, String url, String API, Listener<T> listener,
                                    final String mRequestBody, Type repClass, Response.ErrorListener errorListener) {

        if (BuildConfig.DEBUG) {
            System.out.println("url " + url);
            System.out.println("Body " + mRequestBody);
        }
        GsonRequest<T> myReq = new GsonRequest<T>(
                1, url,
                repClass, null, listener, errorListener) {

            @Override
            public String getBodyContentType() {
                return PROTOCOL_CONTENT_TYPE;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
                } catch (UnsupportedEncodingException uee) {
                    Crashlytics.logException(uee);
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            mRequestBody, PROTOCOL_CHARSET);
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                if (headers == null || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<>();
                    String authenticationKey = AppPrefs.getInstance(context).getSharedPreferences().
                            getString(AppPrefs.PREFS_TOKEN, "");
                    if (authenticationKey != null) {
                        headers.put("Authorization", authenticationKey);
                    }
                }

                return headers;
            }
        };

        RequestQueue mRequestQueue = ((SubKuchApplication) context.getApplicationContext()).getRequestQueue();
        myReq.setTag(API);
        mRequestQueue.add(myReq);

    }

    static <T> void callGetApi(final Context context, String url, String API, Listener<T> listener, Type repClass,
                               Response.ErrorListener errorListener) {

        if (BuildConfig.DEBUG) {
            System.out.println(url);
        }

        GsonRequest<T> myReq = new GsonRequest<T>(
                Request.Method.GET, url,
                repClass, null, listener, errorListener) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=" + PROTOCOL_CHARSET;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                if (headers == null || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<>();
                    String authenticationKey = AppPrefs.getInstance(context).getSharedPreferences().
                            getString(AppPrefs.PREFS_TOKEN, "");
                    if (authenticationKey != null) {
                        headers.put("Authorization", authenticationKey);
                    }
                }

                return headers;

            }
        };

        RequestQueue mRequestQueue = ((SubKuchApplication) context.getApplicationContext()).getRequestQueue();
        myReq.setTag(API);
        mRequestQueue.add(myReq);

    }


    static <T> void callDeleteApi(final Context context, String url, String API, Listener<T> listener, Type repClass,
                                  Response.ErrorListener errorListener) {


        GsonRequest<T> myReq = new GsonRequest<T>(
                3, url,
                repClass, null, listener, errorListener) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=" + PROTOCOL_CHARSET;
            }

            @Override
            protected Response<T> parseNetworkResponse(NetworkResponse response) {
                try {
                    if (response.data.length == 0) {
                        byte[] responseData = "{}".getBytes("UTF-8");
                        response = new NetworkResponse(response.statusCode, responseData, response.headers, response.notModified);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                if (headers == null || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<>();
                }

                return headers;

            }
        };

        RequestQueue mRequestQueue = ((SubKuchApplication) context.getApplicationContext()).getRequestQueue();
        myReq.setTag(API);
        mRequestQueue.add(myReq);

    }

    private static byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            Crashlytics.logException(uee);
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }
}
