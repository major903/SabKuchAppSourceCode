package vedam.subkuch.update;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

interface AppUpdateApi {
    @GET
    Call<AppUpdateManifest> getManifest(@Url String manifestUrl);
}
