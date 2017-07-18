package advisor.nutrition.nutritionadvisor.api.data.fat.secret;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;

public interface FatSecretService {

    @GET("")
    Call<ApiSearchFood> getFood(@Field("method") String method,
                                @Field("format") String format,
                                @Field("food_id") String food_id);

    @GET("")
    Call<ApiSearchFood> searchFood(@Field("method") String method,
                                   @Field("format") String format,
                                   @Field("search_expression") String search_expression);
}
