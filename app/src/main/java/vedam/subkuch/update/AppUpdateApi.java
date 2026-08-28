package vedam.subkuch.update;

import retrofit2.Call;
import retrofit2.http.Streaming;
import retrofit2.http.GET;
import retrofit2.http.Url;
import okhttp3.ResponseBody;

interface AppUpdateApi {
    @GET
    Call<AppUpdateManifest> getManifest(@Url String manifestUrl);

    @Streaming
    @GET
    Call<ResponseBody> downloadApk(@Url String apkUrl);
}
