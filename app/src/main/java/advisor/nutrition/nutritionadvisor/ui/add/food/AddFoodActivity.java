package advisor.nutrition.nutritionadvisor.ui.add.food;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.concurrent.TimeoutException;

import advisor.nutrition.nutritionadvisor.NutritionAdvisorApp;
import advisor.nutrition.nutritionadvisor.R;
import advisor.nutrition.nutritionadvisor.api.ndb.NdbFactory;
import advisor.nutrition.nutritionadvisor.data.Food;
import advisor.nutrition.nutritionadvisor.provider.DataProviderUtils;
import advisor.nutrition.nutritionadvisor.provider.NutritionAdvisorProvider;
import advisor.nutrition.nutritionadvisor.provider.UserDayFoodColumns;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class AddFoodActivity extends AppCompatActivity implements FoodSelectedListener {

    public static final String EXTRA_USER_NAME = "Actions.Extra.AddFood.user_name";
    public static final String EXTRA_DATE = "Actions.Extra.AddFood.date";


    @BindView(R.id.rv_food_search_result)
    RecyclerView recyclerViewSearchResult;
    private FoodsAdapter foodsAdapter;

    @BindView(R.id.et_food_name)
    EditText editTextFoodName;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar progressBarLoadingIndicator;

    private String mUserName;
    private String mDate;

    private Tracker mTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        ButterKnife.bind(this);

        Timber.d("onCreate - entry");

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_USER_NAME) && intent.hasExtra(EXTRA_DATE)) {
            mUserName = intent.getStringExtra(EXTRA_USER_NAME);
            mDate = intent.getStringExtra(EXTRA_DATE);
            Timber.d("open nutrition calculator for user %s and date %s", mUserName, mDate);
        } else {
            Timber.e("not extras defined");
        }

        foodsAdapter = new FoodsAdapter(this, this);
        recyclerViewSearchResult.setAdapter(foodsAdapter);
        recyclerViewSearchResult.setLayoutManager(
                new GridLayoutManager(this, getResources().getInteger(R.integer.gv_food_column_count), LinearLayoutManager.VERTICAL, false));

        mTracker = ((NutritionAdvisorApp) getApplication()).getDefaultTracker();
        mTracker.setScreenName(AddFoodActivity.class.getSimpleName());

        getSupportActionBar().setTitle(R.string.search_for_food);

        Timber.d("onCreate - exit");
    }

    @OnClick(R.id.iv_search)
    public void onSearchClicked() {
        Timber.d("onSearchClicked - entry");
        final String query = editTextFoodName.getText().toString().trim();
        if (query.length() > 0) {
            updateUiLoadingTo(true);
            NdbFactory.getNdbApi().searchFood(query, new Callback<Food[]>() {
                @Override
                public void onResponse(@NonNull Call<Food[]> call, @NonNull Response<Food[]> response) {
                    updateUiLoadingTo(false);
                    foodsAdapter.updateFoods(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<Food[]> call, @NonNull Throwable t) {
                    updateUiLoadingTo(false);
                    if (t instanceof TimeoutException) {
                        Toast.makeText(AddFoodActivity.this, R.string.search_request_timed_out, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(AddFoodActivity.this, R.string.food_not_found, Toast.LENGTH_LONG).show();
                    }
                }
            });

            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Food")
                    .setAction("searched")
                    .setLabel(query)
                    .build());

        } else {
            Timber.d("user queried without any input data");
            Toast.makeText(this, R.string.enter_search_query, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void selected(final Food food) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Timber.d("adding to user %s on date %s the food %s", mUserName, mDate, food.toString());

                DataProviderUtils.insertFood(AddFoodActivity.this, food);

                final ContentValues userDayFoodEntry = new ContentValues();
                userDayFoodEntry.put(UserDayFoodColumns.FOOD_ID, food.getId());
                userDayFoodEntry.put(UserDayFoodColumns.DATE, mDate);
                userDayFoodEntry.put(UserDayFoodColumns.USER_NAME, mUserName);
                userDayFoodEntry.put(UserDayFoodColumns.PREFERENCE_PORTIONS, 0);
                userDayFoodEntry.put(UserDayFoodColumns.CALCULATED_PORTIONS, 0);
                getContentResolver().insert(NutritionAdvisorProvider.UserDayFood.USER_DAY_FOOD, userDayFoodEntry);

                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Food")
                        .setAction("added")
                        .setLabel(String.valueOf(food.getId()))
                        .build());

                finish();
                return null;
            }
        }.execute();

    }

    private void updateUiLoadingTo(boolean loading) {
        if (loading) {
            progressBarLoadingIndicator.setVisibility(View.VISIBLE);
            recyclerViewSearchResult.setVisibility(View.INVISIBLE);
        } else {
            progressBarLoadingIndicator.setVisibility(View.INVISIBLE);
            recyclerViewSearchResult.setVisibility(View.VISIBLE);
        }
    }
}
