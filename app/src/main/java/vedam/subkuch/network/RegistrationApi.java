package vedam.subkuch.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import vedam.subkuch.network.models.DataEntryListResponse;
import vedam.subkuch.network.models.DataEntryRequest;
import vedam.subkuch.network.models.DataEstateRequest;
import vedam.subkuch.network.models.DataNriRequest;
import vedam.subkuch.network.models.DeleteProfileRequest;
import vedam.subkuch.network.models.DeleteProfileResponse;
import vedam.subkuch.network.models.EstateTypeListResponse;
import vedam.subkuch.network.models.RegistrationMasterResponse;
import vedam.subkuch.ui.jobs.models.AddResponse;

/** Typed Retrofit endpoints for the newer registration and contribution API. */
public interface RegistrationApi {

    @GET("api/Master/GetStates")
    Call<RegistrationMasterResponse> getStates();

    @GET("api/Master/GetDistricts")
    Call<RegistrationMasterResponse> getDistricts();

    @GET("api/DataEntry/GetUniqueDataEntries")
    Call<DataEntryListResponse> getUniqueDataEntries(
            @Query("UserId") int userId,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );

    @POST("api/DataEntry/AddDataEntry")
    Call<AddResponse> addDataEntry(@Body DataEntryRequest request);

    @GET("api/Master/GetCountries")
    Call<RegistrationMasterResponse> getCountries();

    @GET("api/EstateType/GetEstateTypes")
    Call<EstateTypeListResponse> getEstateTypes();

    @POST("api/DataNRI/AddDataNRI")
    Call<AddResponse> addDataNri(@Body DataNriRequest request);

    @POST("api/DataEstate/AddDataEstate")
    Call<AddResponse> addDataEstate(@Body DataEstateRequest request);

    @POST("api/UserProfile/DeleteProfile")
    Call<DeleteProfileResponse> deleteProfile(@Body DeleteProfileRequest request);

    @GET("api/DataNRI/GetDataNRIs")
    Call<DataEntryListResponse> getDataNris(
            @Query("userId") int userId,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );

    /** Real estate entries; null filters are skipped by Retrofit. */
    @GET("api/DataEstate/GetDataEstates")
    Call<DataEntryListResponse> getDataEstates(
            @Query("userId") Integer userId,
            @Query("estateTypeId") Integer estateTypeId,
            @Query("districtId") Integer districtId,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );
}
