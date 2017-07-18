package advisor.nutrition.nutritionadvisor.api.ndb;

import advisor.nutrition.nutritionadvisor.api.ndb.data.NdbFoodReportResponse;
import advisor.nutrition.nutritionadvisor.api.ndb.data.NdbSearchResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface NdbService {

    @GET("search")
    Call<NdbSearchResponse> search(
            @Query("api_key") String apiKey,
            @Query("format") String format,
            @Query("offset") String offset,
            @Query("ds") String ds,
            @Query("sort") String sort,
            @Query("max") String max,
            @Query("q") String q
    );

    @GET("reports")
    Call<NdbFoodReportResponse> foodReport(
            @Query("api_key") String apiKey,
            @Query("format") String format,
            @Query("ndbno") String ndbNumber,
            @Query("type") String type
    );
}
