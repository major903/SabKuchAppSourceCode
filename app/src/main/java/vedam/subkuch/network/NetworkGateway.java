package vedam.subkuch.network;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.SocketTimeoutException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;
import vedam.subkuch.BuildConfig;
import vedam.subkuch.network.models.DataPart;
import vedam.subkuch.utils.AppPrefs;

/**
 * Retrofit-backed network gateway kept source-compatible with the old callers.
 *
 * Existing screens receive the same callback shape while every request is
 * executed by Retrofit and OkHttp.
 */
public final class NetworkGateway {

    private static final long TIMEOUT_SECONDS = 30L;
    private static final String JSON_MEDIA_TYPE = "application/json; charset=utf-8";
    private static final String FORM_MEDIA_TYPE = "application/x-www-form-urlencoded; charset=utf-8";
    private static final Gson GSON = new Gson();
    private static final Map<String, CopyOnWriteArrayList<Call<ResponseBody>>> CALLS_BY_TAG =
            new ConcurrentHashMap<>();
    private static DynamicApi api;

    private NetworkGateway() {
    }

    public static void cancelRequest(Context context, String tag) {
        if (tag == null) {
            return;
        }
        List<Call<ResponseBody>> calls = CALLS_BY_TAG.remove(tag);
        if (calls == null) {
            return;
        }
        for (Call<ResponseBody> call : calls) {
            call.cancel();
        }
    }

    static <T> void callApiWithJson(Context context, String url, String API, Response.Listener<T> listener,
                                    String json, Type responseType, Response.ErrorListener errorListener,
                                    HashMap<String, String> params) {
        RequestBody body;
        if (params != null && !params.isEmpty()) {
            FormBody.Builder form = new FormBody.Builder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                form.add(entry.getKey(), entry.getValue() == null ? "" : entry.getValue());
            }
            body = form.build();
        } else {
            body = requestBody(json, FORM_MEDIA_TYPE);
        }
        enqueue(API, api().post(url, authHeaders(context), body), responseType, listener, errorListener);
    }

    static <T> void callApiWithBody(Context context, String url, String API, Response.Listener<T> listener,
                                    String requestBody, Type responseType,
                                    Response.ErrorListener errorListener) {
        callApiWithBody(context, url, API, listener, requestBody, responseType, errorListener, true);
    }

    /** Sends a write request once. Retrofit does not retry a failed Call automatically. */
    static <T> void callApiWithBodyNoRetry(Context context, String url, String API,
                                           Response.Listener<T> listener, String requestBody,
                                           Type responseType, Response.ErrorListener errorListener) {
        callApiWithBody(context, url, API, listener, requestBody, responseType, errorListener, false);
    }

    private static <T> void callApiWithBody(Context context, String url, String API,
                                            Response.Listener<T> listener, String requestBody,
                                            Type responseType, Response.ErrorListener errorListener,
                                            boolean allowRetry) {
        // Retrofit has no automatic retry policy. The flag is retained for source compatibility.
        enqueue(API, api().post(url, authHeaders(context), requestBody(requestBody, JSON_MEDIA_TYPE)),
                responseType, listener, errorListener);
    }

    static <T> void callGetApi(Context context, String url, String API, Response.Listener<T> listener,
                               Type responseType, Response.ErrorListener errorListener) {
        enqueue(API, api().get(url, authHeaders(context)), responseType, listener, errorListener);
    }

    static <T> void callGetApiWithHeaders(Context context, String url, String API,
                                          Response.Listener<T> listener, Type responseType,
                                          Response.ErrorListener errorListener,
                                          Map<String, String> additionalHeaders) {
        Map<String, String> headers = authHeaders(context);
        if (additionalHeaders != null) {
            headers.putAll(additionalHeaders);
        }
        enqueue(API, api().get(url, headers), responseType, listener, errorListener);
    }

    static <T> void callDeleteApi(Context context, String url, String API, Response.Listener<T> listener,
                                  Type responseType, Response.ErrorListener errorListener) {
        enqueue(API, api().delete(url, authHeaders(context)), responseType, listener, errorListener);
    }

    static <T> void callApiWithMultipartBody(Context context, String url, String API,
                                             Response.Listener<T> listener,
                                             Map<String, String> requestParams,
                                             Map<String, DataPart> requestData,
                                             Type responseType,
                                             Response.ErrorListener errorListener) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        if (requestParams != null) {
            for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                RequestBody value = requestBody(entry.getValue() == null ? "" : entry.getValue(),
                        "text/plain; charset=utf-8");
                parts.add(MultipartBody.Part.createFormData(entry.getKey(), null, value));
            }
        }
        if (requestData != null) {
            for (Map.Entry<String, DataPart> entry : requestData.entrySet()) {
                DataPart dataPart = entry.getValue();
                if (dataPart == null || dataPart.getContent() == null) {
                    continue;
                }
                String mediaType = dataPart.getType();
                if (mediaType == null || mediaType.trim().isEmpty()) {
                    mediaType = "application/octet-stream";
                }
                RequestBody file = RequestBody.create(MediaType.parse(mediaType), dataPart.getContent());
                parts.add(MultipartBody.Part.createFormData(entry.getKey(), dataPart.getFileName(), file));
            }
        }
        enqueue(API, api().multipart(url, authHeaders(context), parts), responseType, listener, errorListener);
    }

    private static RequestBody requestBody(String value, String mediaType) {
        return RequestBody.create(MediaType.parse(mediaType), value == null ? "" : value);
    }

    private static Map<String, String> authHeaders(Context context) {
        Map<String, String> headers = new HashMap<>();
        String token = AppPrefs.getPrefsToken(context);
        if (token != null && !token.trim().isEmpty()) {
            headers.put("Authorization", token);
        }
        return headers;
    }

    private static synchronized DynamicApi api() {
        if (api == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(BuildConfig.DEBUG
                    ? HttpLoggingInterceptor.Level.BASIC
                    : HttpLoggingInterceptor.Level.NONE);
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .addInterceptor(logging)
                    .build();
            api = new Retrofit.Builder()
                    .baseUrl("https://placeholder.invalid/")
                    .client(client)
                    .build()
                    .create(DynamicApi.class);
        }
        return api;
    }

    private static <T> void enqueue(String tag, Call<ResponseBody> call, Type responseType,
                                    Response.Listener<T> listener,
                                    Response.ErrorListener errorListener) {
        if (tag != null) {
            CALLS_BY_TAG.computeIfAbsent(tag, ignored -> new CopyOnWriteArrayList<>()).add(call);
        }
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> completedCall,
                                   retrofit2.Response<ResponseBody> response) {
                removeCall(tag, completedCall);
                if (!response.isSuccessful()) {
                    String errorBody = "";
                    try {
                        errorBody = response.errorBody() == null ? "" : response.errorBody().string();
                    } catch (IOException ignored) {
                    }
                    notifyError(errorListener, new ApiError(new NetworkResponse(
                            response.code(), errorBody.getBytes())));
                    return;
                }
                try {
                    String raw = response.body() == null ? "" : response.body().string();
                    T value = parse(raw, responseType);
                    if (listener != null) {
                        listener.onResponse(value);
                    }
                } catch (Exception exception) {
                    notifyError(errorListener, new ApiError(exception));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> failedCall, Throwable throwable) {
                removeCall(tag, failedCall);
                if (throwable instanceof SocketTimeoutException) {
                    notifyError(errorListener, new TimeoutError(throwable));
                } else if (throwable instanceof IOException) {
                    notifyError(errorListener, new NetworkError(throwable));
                } else {
                    notifyError(errorListener, new ApiError(throwable));
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static <T> T parse(String raw, Type responseType) {
        if (responseType == null || responseType == Void.class) {
            return null;
        }
        if (responseType == String.class) {
            return (T) raw;
        }
        return GSON.fromJson(raw == null || raw.trim().isEmpty() ? "{}" : raw, responseType);
    }

    private static void removeCall(String tag, Call<ResponseBody> call) {
        if (tag == null) {
            return;
        }
        CopyOnWriteArrayList<Call<ResponseBody>> calls = CALLS_BY_TAG.get(tag);
        if (calls == null) {
            return;
        }
        calls.remove(call);
        if (calls.isEmpty()) {
            CALLS_BY_TAG.remove(tag, calls);
        }
    }

    private static void notifyError(Response.ErrorListener listener, ApiError error) {
        if (listener != null) {
            listener.onErrorResponse(error);
        }
    }

    private interface DynamicApi {
        @GET
        Call<ResponseBody> get(@Url String url, @HeaderMap Map<String, String> headers);

        @POST
        Call<ResponseBody> post(@Url String url, @HeaderMap Map<String, String> headers, @Body RequestBody body);

        @DELETE
        Call<ResponseBody> delete(@Url String url, @HeaderMap Map<String, String> headers);

        @Multipart
        @POST
        Call<ResponseBody> multipart(@Url String url, @HeaderMap Map<String, String> headers,
                                     @Part List<MultipartBody.Part> parts);
    }
}
