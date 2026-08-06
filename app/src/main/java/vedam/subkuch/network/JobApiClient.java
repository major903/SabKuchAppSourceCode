package vedam.subkuch.network;

import vedam.subkuch.network.Response;
import vedam.subkuch.network.ApiError;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vedam.subkuch.BuildConfig;
import vedam.subkuch.SabkuchApplication;
import vedam.subkuch.utils.AppPrefs;

/** Shared Retrofit client and compatibility bridge for the existing Jobs callbacks. */
public final class JobApiClient {

    private static final long TIMEOUT_SECONDS = 30L;
    private static final Gson GSON = new Gson();
    private static JobApi api;

    private JobApiClient() {
    }

    public static synchronized JobApi getApi() {
        if (api == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(BuildConfig.DEBUG
                    ? HttpLoggingInterceptor.Level.BASIC
                    : HttpLoggingInterceptor.Level.NONE);
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .addInterceptor(chain -> {
                        okhttp3.Request request = chain.request();
                        String token = AppPrefs.getPrefsToken(
                                SabkuchApplication.getInstance().getApplicationContext());
                        if (token == null || token.trim().isEmpty()) {
                            return chain.proceed(request);
                        }
                        return chain.proceed(request.newBuilder()
                                .header("Authorization", token)
                                .build());
                    })
                    .addInterceptor(logging)
                    .build();
            api = new Retrofit.Builder()
                    .baseUrl(NetworkConstants.JOB_END_POINT + "/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(JobApi.class);
        }
        return api;
    }

    public static <T> void enqueue(
            Call<ResponseBody> call,
            Type responseType,
            Response.Listener<T> listener,
            Response.ErrorListener errorListener
    ) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (!response.isSuccessful() || response.body() == null) {
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
                    T value = GSON.fromJson(response.body().string(), responseType);
                    if (listener != null) listener.onResponse(value);
                } catch (Exception exception) {
                    notifyError(errorListener, new ApiError(exception));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
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

    private static void notifyError(Response.ErrorListener listener, ApiError error) {
        if (listener != null) listener.onErrorResponse(error);
    }
}
