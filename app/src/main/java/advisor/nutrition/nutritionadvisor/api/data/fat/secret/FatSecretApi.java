package advisor.nutrition.nutritionadvisor.api.data.fat.secret;

import advisor.nutrition.nutritionadvisor.data.Food;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FatSecretApi {

    private static FatSecretService fatSecretService;

    public static FatSecretService getFatSecretApi() {
        if (fatSecretService == null){
            fatSecretService = FatSecretRetrofitFactory.getRetrofitInstance().create(FatSecretService.class);
        }
        return fatSecretService;
    }

    public void searchForFood(final String foodExpression, final Callback<Food> foodCallback){
        fatSecretService.searchFood("food.search", "json", foodExpression)
                .enqueue(new Callback<ApiSearchFood>() {
                    @Override
                    public void onResponse(Call<ApiSearchFood> call, Response<ApiSearchFood> response) {

                    }

                    @Override
                    public void onFailure(Call<ApiSearchFood> call, Throwable t) {

                    }
                });
    }


}
