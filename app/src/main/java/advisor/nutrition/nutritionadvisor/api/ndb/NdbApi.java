package advisor.nutrition.nutritionadvisor.api.ndb;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import advisor.nutrition.nutritionadvisor.BuildConfig;
import advisor.nutrition.nutritionadvisor.api.ndb.data.ConverterUtils;
import advisor.nutrition.nutritionadvisor.api.ndb.data.NdbFoodReportResponse;
import advisor.nutrition.nutritionadvisor.api.ndb.data.NdbSearchResponse;
import advisor.nutrition.nutritionadvisor.data.Food;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

@SuppressWarnings("FieldCanBeLocal")
public class NdbApi {
    private final NdbService mNdbiService;
    private String JSON_FORMAT = "json";
    private String FOOD_REFERENCE = "Standard Reference";
    private String SORTED_BY_RELEVENCE = "r";
    private String REPORT_TYPE_BASIC = "b";
    private String NUMBER_OF_SEARCH_ITEMS = "10";

    NdbApi(NdbService ndbService) {
        this.mNdbiService = ndbService;
    }

    public void searchFood(final String query, final advisor.nutrition.nutritionadvisor.api.Callback<Food[]> callback) {
        Timber.i("search ndb for query %s", query);
        mNdbiService.search(BuildConfig.USDA_DB_API_KEY,
                JSON_FORMAT, "0", FOOD_REFERENCE, SORTED_BY_RELEVENCE, NUMBER_OF_SEARCH_ITEMS, query)
                .enqueue(new Callback<NdbSearchResponse>() {
                    @Override
                    public void onResponse(Call<NdbSearchResponse> call, Response<NdbSearchResponse> response) {
                        final NdbSearchResponse.FoodList[] items = response.body().getList().getItem();
                        final int itemsLength = items.length;

                        Timber.i("ndb search query returned with %d items", itemsLength);

                        final Food[] foods = new Food[itemsLength];
                        final CountDownLatch latch = new CountDownLatch(itemsLength);
                        for (int i = 0; i < itemsLength; i++) {
                            final int index = i;
                            NdbApi.this.foodReport(items[i].getNdbNumber(),
                                    new advisor.nutrition.nutritionadvisor.api.Callback<Food>() {
                                        @Override
                                        public void response(final Food food) {
                                            foods[index] = food;
                                            latch.countDown();
                                        }
                                    });
                        }
                        try {
                            latch.await(2, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                            Timber.e("timeout or interrupt occurred while waiting for ndb db response");
                            e.printStackTrace();
                        }
                        Timber.d("successfully queried %s", query);
                        callback.response(foods);
                    }

                    @Override
                    public void onFailure(Call<NdbSearchResponse> call, Throwable t) {
                        Timber.e("failed to query %s in the ndb search query", query);
                    }
                });
    }

    public void foodReport(final String ndbNumber, final advisor.nutrition.nutritionadvisor.api.Callback<Food> callback) {
        Timber.i("food report query ndb for ndbNumber %s", ndbNumber);
        mNdbiService.foodReport(BuildConfig.USDA_DB_API_KEY, JSON_FORMAT, ndbNumber, REPORT_TYPE_BASIC)
                .enqueue(new Callback<NdbFoodReportResponse>() {
                    @Override
                    public void onResponse(Call<NdbFoodReportResponse> call, Response<NdbFoodReportResponse> response) {
                        Timber.d("food report for %s was successful", ndbNumber);
                        try {
                            callback.response(
                                    ConverterUtils.convertNdbFoodResponse(
                                            response.body()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<NdbFoodReportResponse> call, Throwable t) {
                        Timber.e("failed to report for food number %s in the ndb report query", ndbNumber);
                    }
                });
    }

}
