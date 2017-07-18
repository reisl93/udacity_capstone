package advisor.nutrition.nutritionadvisor.api.data.fat.secret;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FatSecretRetrofitFactory {

    private static Retrofit retrofitInstance;

    public static Retrofit getRetrofitInstance() {
        if (retrofitInstance == null){

            /*OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(
                    BuildConfig.FAT_SECRET_DB_API_CONSUMER_KEY,
                    BuildConfig.FAT_SECRET_DB_API_SHARED_SECRET);
            consumer.setTokenWithSecret(String.valueOf(Math.random()*100000), String.valueOf(Math.random()*100000));

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new SigningInterceptor(consumer))
                    .build();*/

            retrofitInstance = new Retrofit.Builder()
                    .baseUrl("http://platform.fatsecret.com/rest/server.api")
                    .addConverterFactory(GsonConverterFactory.create())
              //      .client(okHttpClient)
                    .build();
        }

        return retrofitInstance;
    }
}
