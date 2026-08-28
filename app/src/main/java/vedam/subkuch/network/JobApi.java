package vedam.subkuch.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/** Retrofit endpoints used by the Jobs and Job Mela features. */
public interface JobApi {

    @GET("https://sabkuch.sabkuchworld.com/AllAPI/GetAllCity")
    Call<ResponseBody> getCities();

    @GET("api/Master/GetJobTypes")
    Call<ResponseBody> getJobTypes();
}
