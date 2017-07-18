package advisor.nutrition.nutritionadvisor.api.ndb;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

    public void searchFood(final String query, final Callback<Food[]> callback) {
        Timber.i("search ndb for query %s", query);
        mNdbiService.search(BuildConfig.USDA_DB_API_KEY,
                JSON_FORMAT, "0", FOOD_REFERENCE, SORTED_BY_RELEVENCE, NUMBER_OF_SEARCH_ITEMS, query)
                .enqueue(new Callback<NdbSearchResponse>() {
                    @Override
                    public void onResponse(Call<NdbSearchResponse> call, Response<NdbSearchResponse> response) {
                        try {
                            final NdbSearchResponse.FoodList[] items = response.body().getList().getItem();
                            final int itemsLength = items.length;

                            Timber.i("ndb search query returned with %d items", itemsLength);

                            final Food[] foods = new Food[itemsLength];
                            final CountDownLatch latch = new CountDownLatch(itemsLength);
                            for (int i = 0; i < itemsLength; i++) {
                                final int index = i;
                                NdbApi.this.foodReport(items[i].getNdbNumber(),
                                        new Callback<Food>() {
                                            @Override
                                            public void onResponse(Call<Food> call, Response<Food> response) {
                                                foods[index] = response.body();
                                                latch.countDown();
                                            }

                                            @Override
                                            public void onFailure(Call<Food> call, Throwable t) {
                                                //ignore this one
                                            }
                                        });
                            }

                            latch.await(15, TimeUnit.SECONDS);

                            callback.onResponse(null, Response.success(foods));
                            Timber.d("successfully queried %s", query);
                        } catch (InterruptedException e) {
                            Timber.e("timeout or interrupt occurred while waiting for ndb db response");
                            callback.onFailure(null, new TimeoutException("search request exceeded 5 seconds"));
                        } catch (Throwable e) {
                            Timber.e(e, "an error occurred while searching");
                            callback.onFailure(null, e);
                        }
                    }

                    @Override
                    public void onFailure(Call<NdbSearchResponse> call, Throwable t) {
                        Timber.e("failed to query %s in the ndb search query", query);
                        callback.onFailure(null, t);
                    }
                });
    }

    public void foodReport(final String ndbNumber, final Callback<Food> callback) {
        Timber.i("food report query ndb for ndbNumber %s", ndbNumber);
        mNdbiService.foodReport(BuildConfig.USDA_DB_API_KEY, JSON_FORMAT, ndbNumber, REPORT_TYPE_BASIC)
                .enqueue(new Callback<NdbFoodReportResponse>() {
                    @Override
                    public void onResponse(Call<NdbFoodReportResponse> call, Response<NdbFoodReportResponse> response) {
                        Timber.d("food report for %s was successful", ndbNumber);
                        try {
                            callback.onResponse(null, Response.success(
                                    ConverterUtils.convertNdbFoodResponse(
                                            response.body())));
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.onFailure(null, e);
                        }
                    }

                    @Override
                    public void onFailure(Call<NdbFoodReportResponse> call, Throwable t) {
                        Timber.e("failed to report for food number %s in the ndb report query", ndbNumber);
                    }
                });
    }

}
