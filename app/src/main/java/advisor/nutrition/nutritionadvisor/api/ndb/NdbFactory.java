package advisor.nutrition.nutritionadvisor.api.ndb;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NdbFactory {
    private static Retrofit retrofitInstance;
    private static NdbApi ndbApi;

    public static NdbApi getNdbApi() {
        if (ndbApi == null){
            ndbApi = new NdbApi(getRetrofitInstance().create(NdbService.class));
        }
        return ndbApi;
    }

    private static Retrofit getRetrofitInstance() {
        if (retrofitInstance == null){
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl("https://api.nal.usda.gov/ndb/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitInstance;
    }


}
