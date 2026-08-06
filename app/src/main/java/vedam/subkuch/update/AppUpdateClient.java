package vedam.subkuch.update;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vedam.subkuch.BuildConfig;

/** Retrofit client for the public GitHub update manifest. */
final class AppUpdateClient {
    private static final String BASE_URL = "https://raw.githubusercontent.com/";
    private static AppUpdateApi api;

    private AppUpdateClient() {
    }

    static synchronized AppUpdateApi getApi() {
        if (api == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(BuildConfig.DEBUG
                    ? HttpLoggingInterceptor.Level.BASIC
                    : HttpLoggingInterceptor.Level.NONE);
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .addInterceptor(logging)
                    .build();
            api = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .build()
                    .create(AppUpdateApi.class);
        }
        return api;
    }

    static void check(Callback<AppUpdateManifest> callback) {
        getApi().getManifest(BuildConfig.APP_UPDATE_MANIFEST_URL).enqueue(callback);
    }
}
