package advisor.nutrition.nutritionadvisor.ui.add.food;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import advisor.nutrition.nutritionadvisor.R;
import advisor.nutrition.nutritionadvisor.api.Callback;
import advisor.nutrition.nutritionadvisor.api.ndb.NdbFactory;
import advisor.nutrition.nutritionadvisor.data.Food;
import advisor.nutrition.nutritionadvisor.provider.FoodColumns;
import advisor.nutrition.nutritionadvisor.provider.NutritionAdvisorProvider;
import advisor.nutrition.nutritionadvisor.provider.UserDayFoodColumns;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        ButterKnife.bind(this);

        Timber.d("onCreate - entry");

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_USER_NAME) && intent.hasExtra(EXTRA_DATE)){
            mUserName = intent.getStringExtra(EXTRA_USER_NAME);
            mDate = intent.getStringExtra(EXTRA_DATE);
            Timber.d("open nutrition calculator for user %s and date %s", mUserName, mDate);
        } else {
            Timber.e("not extras defined");
            Toast.makeText(this, "Internal Error occurred", Toast.LENGTH_LONG).show();
        }

        foodsAdapter = new FoodsAdapter(this, this);
        recyclerViewSearchResult.setAdapter(foodsAdapter);
        recyclerViewSearchResult.setLayoutManager(
                new GridLayoutManager(this, getResources().getInteger(R.integer.gv_food_column_count), LinearLayoutManager.VERTICAL, false));

        Timber.d("onCreate - exit");
    }

    @OnClick(R.id.iv_search)
    public void onSearchClicked(){
        Timber.d("onSearchClicked - entry");
        final String query = editTextFoodName.getText().toString().trim();
        if (query.length() > 0) {
            updateUiLoadingTo(true);
            NdbFactory.getNdbApi().searchFood(query, new Callback<Food[]>() {
                @Override
                public void response(Food[] food) {
                    updateUiLoadingTo(false);
                    foodsAdapter.updateFoods(food);
                }
            });
        } else {
            Timber.d("user queried without any input data");
            Toast.makeText(this, "please enter a search query", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void selected(Food food) {
        Timber.d("adding to user %s on date %s the food %s", mUserName, mDate, food.toString());

        final ContentValues foodEntry = new ContentValues();
        foodEntry.put(FoodColumns._ID, food.getId());
        foodEntry.put(FoodColumns.CARBOHYDRATES, food.getCarbs());
        foodEntry.put(FoodColumns.FAT, food.getFat());
        foodEntry.put(FoodColumns.PROTEIN, food.getProteins());
        foodEntry.put(FoodColumns.NAME, food.getName());
        foodEntry.put(FoodColumns.PORTION_SIZE, food.getPortionSize());
        foodEntry.put(FoodColumns.MEASURE, food.getMeasure());
        getContentResolver().insert(NutritionAdvisorProvider.Foods.FOODS, foodEntry);

        final ContentValues userDayFoodEntry = new ContentValues();
        userDayFoodEntry.put(UserDayFoodColumns.FOOD_ID, food.getId());
        userDayFoodEntry.put(UserDayFoodColumns.DATE, mDate);
        userDayFoodEntry.put(UserDayFoodColumns.USER_NAME, mUserName);
        userDayFoodEntry.put(UserDayFoodColumns.TARGET_PORTIONS, 0);
        userDayFoodEntry.put(UserDayFoodColumns.CALCULATED_PORTIONS, 0);
        getContentResolver().insert(NutritionAdvisorProvider.UserDayFood.USER_DAY_FOOD, userDayFoodEntry);

        finish();
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
