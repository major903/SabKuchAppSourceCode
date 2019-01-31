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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import vedam.subkuch.BuildConfig;
import vedam.subkuch.SubKuchApplication;
import vedam.subkuch.utils.AppPrefs;

/**
 * @author gour
 */
public class HelperVolley<T> {

    private static final String twoHyphens = "--";
    private static final String lineEnd = "\r\n";
    private static final String boundary = "apiclient-" + System.currentTimeMillis();

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

    static <T> void callApiWithMultipartBody(Context context, String url, String API, Listener<T> listener, Map<String, String> mRequestParams,
                                             Map<String, DataPart> mRequestData, Type repClass, Response.ErrorListener errorListener) {

        if (BuildConfig.DEBUG) {
            System.out.println("url " + url);
            System.out.println("Params " + mRequestParams);
            System.out.println("Data " + mRequestData);
        }
        GsonRequest<T> myReq = new GsonRequest<T>(
                1, url,
                repClass, null, listener, errorListener) {

            @Override
            public String getBodyContentType() {
                return "multipart/form-data;boundary=" + boundary;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos);

                try {
                    // populate text payload
                    if (mRequestParams != null && mRequestParams.size() > 0) {
                        textParse(dos, mRequestParams, getParamsEncoding());
                    }

                    // populate data byte payload
                    if (mRequestData != null && mRequestData.size() > 0) {
                        dataParse(dos, mRequestData);
                    }

                    // close multipart form data after text and file data
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    return bos.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
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

    /**
     * Parse string map into data output stream by key and value.
     *
     * @param dataOutputStream data output stream handle string parsing
     * @param params           string inputs collection
     * @param encoding         encode the inputs, default UTF-8
     * @throws IOException
     */
    private static void textParse(DataOutputStream dataOutputStream, Map<String, String> params, String encoding) throws IOException {
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buildTextPart(dataOutputStream, entry.getKey(), entry.getValue());
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + encoding, uee);
        }
    }

    /**
     * Parse data into data output stream.
     *
     * @param dataOutputStream data output stream handle file attachment
     * @param data             loop through data
     * @throws IOException
     */
    private static void dataParse(DataOutputStream dataOutputStream, Map<String, DataPart> data) throws IOException {
        for (Map.Entry<String, DataPart> entry : data.entrySet()) {
            buildDataPart(dataOutputStream, entry.getValue(), entry.getKey());
        }
    }

    /**
     * Write string data into header and data output stream.
     *
     * @param dataOutputStream data output stream handle string parsing
     * @param parameterName    name of input
     * @param parameterValue   value of input
     * @throws IOException
     */
    private static void buildTextPart(DataOutputStream dataOutputStream, String parameterName, String parameterValue) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"" + lineEnd);
        //dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);
        dataOutputStream.writeBytes(parameterValue + lineEnd);
    }

    /**
     * Write data file into header and data output stream.
     *
     * @param dataOutputStream data output stream handle data parsing
     * @param dataFile         data byte as DataPart from collection
     * @param inputName        name of data input
     * @throws IOException
     */
    private static void buildDataPart(DataOutputStream dataOutputStream, DataPart dataFile, String inputName) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                inputName + "\"; filename=\"" + dataFile.getFileName() + "\"" + lineEnd);
        if (dataFile.getType() != null && !dataFile.getType().trim().isEmpty()) {
            dataOutputStream.writeBytes("Content-Type: " + dataFile.getType() + lineEnd);
        }
        dataOutputStream.writeBytes(lineEnd);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(dataFile.getContent());
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(lineEnd);
    }
}
