package advisor.nutrition.nutritionadvisor.ui.nutrition.calculator;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import advisor.nutrition.nutritionadvisor.R;
import advisor.nutrition.nutritionadvisor.ui.add.food.AddFoodActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class NutritionCalculatorActivity extends AppCompatActivity {

    public static final String EXTRA_USER_NAME = "Actions.Extra.NutrtionCalculator.user_name";
    public static final String EXTRA_DATE = "Actions.Extra.NutrtionCalculator.date";

    @BindView(R.id.rv_foods)
    RecyclerView recyclerViewFoods;

    private String mDate;
    private String mUserName;
    private FoodsAdapter foodsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_calculator);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_USER_NAME) && intent.hasExtra(EXTRA_DATE)){
            mUserName = intent.getStringExtra(EXTRA_USER_NAME);
            mDate = intent.getStringExtra(EXTRA_DATE);
            Timber.d("open nutrition calculator for user %s and date %s", mUserName, mDate);
        } else {
            Timber.e("no extras defined");
            Toast.makeText(this, "Internal Error occurred", Toast.LENGTH_LONG).show();
        }

        getSupportLoaderManager().restartLoader(FoodsForSpecificDayLoader.LOADER_ID, null, new ThisFoodForSpecificDayLoader(this, mDate, mUserName));

        recyclerViewFoods.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.gv_food_column_count), LinearLayoutManager.VERTICAL, false));
        foodsAdapter = new FoodsAdapter(this);
        recyclerViewFoods.setAdapter(foodsAdapter);
    }

    private class ThisFoodForSpecificDayLoader extends FoodsForSpecificDayLoader {
        ThisFoodForSpecificDayLoader(Context mContext, String mDate, String mUsername) {
            super(mContext, mDate, mUsername);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            foodsAdapter.updateFoodCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            foodsAdapter.updateFoodCursor(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(FoodsForSpecificDayLoader.LOADER_ID, null, new ThisFoodForSpecificDayLoader(this, mDate, mUserName));
    }

    @OnClick(R.id.fab_add_food)
    public void addFoodFABClicked(){
        final Intent startAddFoodIntent = new Intent(this, AddFoodActivity.class);
        startAddFoodIntent.putExtra(AddFoodActivity.EXTRA_USER_NAME, mUserName);
        startAddFoodIntent.putExtra(AddFoodActivity.EXTRA_DATE, mDate);
        startActivity(startAddFoodIntent);
    }
}
