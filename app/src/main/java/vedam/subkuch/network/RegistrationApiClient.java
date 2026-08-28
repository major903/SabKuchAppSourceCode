package vedam.subkuch.network;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vedam.subkuch.BuildConfig;
import vedam.subkuch.utils.AppPrefs;

/** Provides the Retrofit client for APIs hosted at REGISTRATION_API_BASE_URL. */
public final class RegistrationApiClient {

    private static final long TIMEOUT_SECONDS = 30L;
    private static Retrofit retrofit;
    private static RegistrationApi api;
    private static String configuredBaseUrl;

    private RegistrationApiClient() {
    }

    public static boolean isConfigured() {
        return !BuildConfig.REGISTRATION_API_BASE_URL.trim().isEmpty();
    }

    /**
     * Builds a registration API client that authenticates every request using the current
     * signed-in user's bearer token. The token is read for each request so a newly signed-in
     * user does not require the Retrofit client to be rebuilt.
     */
    public static synchronized RegistrationApi getApi(Context context) {
        if (!isConfigured()) {
            throw new IllegalStateException("Registration API base URL is not configured");
        }
        if (context == null) {
            throw new IllegalArgumentException("A context is required for registration API requests");
        }

        String baseUrl = BuildConfig.REGISTRATION_API_BASE_URL.trim();
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        if (api == null || !baseUrl.equals(configuredBaseUrl)) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(BuildConfig.DEBUG
                    ? HttpLoggingInterceptor.Level.BASIC
                    : HttpLoggingInterceptor.Level.NONE);
            Context appContext = context.getApplicationContext();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .addInterceptor(chain -> {
                        String token = AppPrefs.getPrefsToken(appContext);
                        if (token == null || token.trim().isEmpty()) {
                            return chain.proceed(chain.request());
                        }
                        return chain.proceed(chain.request().newBuilder()
                                .header("Authorization", token)
                                .build());
                    })
                    .addInterceptor(logging)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            api = retrofit.create(RegistrationApi.class);
            configuredBaseUrl = baseUrl;
        }
        return api;
    }

    /**
     * Exposes the configured Retrofit instance for new coroutine-based feature APIs while
     * existing callback callers continue to use {@link #getApi(Context)} unchanged.
     */
    public static synchronized Retrofit getRetrofit(Context context) {
        getApi(context);
        return retrofit;
    }
}
