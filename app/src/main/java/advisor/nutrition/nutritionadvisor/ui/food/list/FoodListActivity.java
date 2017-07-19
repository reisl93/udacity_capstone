package advisor.nutrition.nutritionadvisor.ui.food.list;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import advisor.nutrition.nutritionadvisor.R;
import advisor.nutrition.nutritionadvisor.data.Day;
import advisor.nutrition.nutritionadvisor.ui.loaders.UserDayFoodsLoader;
import advisor.nutrition.nutritionadvisor.ui.nutrition.calculator.NutritionCalculatorActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class FoodListActivity extends AppCompatActivity {

    public static final String EXTRA_USER_NAME = "Actions.Extra.FoodList.user_name";
    public static final String EXTRA_DATE = "Actions.Extra.FoodList.date";

    private String mUserName;
    private String mDate;

    @BindView(R.id.rv_food_list)
    RecyclerView recyclerViewSearchResult;
    private FoodsAdapter foodsAdapter;

    @BindView(R.id.tv_carbs_calculated)
    TextView textViewCarbs;
    @BindView(R.id.tv_fat_calculated)
    TextView textViewFat;
    @BindView(R.id.tv_proteins_calculated)
    TextView textViewProteins;

    @BindView(R.id.adView)
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        ButterKnife.bind(this);

        Timber.d("onCreate - entry");

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_USER_NAME) && intent.hasExtra(EXTRA_DATE)) {
            mUserName = intent.getStringExtra(EXTRA_USER_NAME);
            mDate = intent.getStringExtra(EXTRA_DATE);
            Timber.d("open food list for user %s and date %s", mUserName, mDate);
        } else {
            Timber.e("not extras defined");
            Toast.makeText(this, "Internal Error occurred", Toast.LENGTH_LONG).show();
        }

        foodsAdapter = new FoodsAdapter();
        recyclerViewSearchResult.setAdapter(foodsAdapter);
        recyclerViewSearchResult.setLayoutManager(
                new GridLayoutManager(this, getResources().getInteger(R.integer.gv_food_list_column_count), LinearLayoutManager.VERTICAL, false));

        getSupportLoaderManager().restartLoader(UserDayFoodsLoader.LOADER_ID, null, new ThisUserDayFoodsLoader(this, mDate, mUserName));

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private class ThisUserDayFoodsLoader extends UserDayFoodsLoader {
        ThisUserDayFoodsLoader(Context mContext, String mDate, String mUsername) {
            super(mContext, mDate, mUsername);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            Day day = new Day();
            updateDay(day, data);
            foodsAdapter.updateFoodList(day.getFoodList());

            textViewCarbs.setText(String.valueOf(day.getCalculatedCarbs()));
            textViewFat.setText(String.valueOf(day.getCalculatedFat()));
            textViewProteins.setText(String.valueOf(day.getCalculatedProteins()));
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            foodsAdapter.updateFoodList(null);
        }
    }

}