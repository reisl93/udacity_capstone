package advisor.nutrition.nutritionadvisor.ui.nutrition.calculator;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

import advisor.nutrition.nutritionadvisor.R;
import advisor.nutrition.nutritionadvisor.data.Day;
import advisor.nutrition.nutritionadvisor.data.Food;
import advisor.nutrition.nutritionadvisor.provider.NutritionAdvisorProvider;
import advisor.nutrition.nutritionadvisor.provider.UserDayColumns;
import advisor.nutrition.nutritionadvisor.ui.add.food.AddFoodActivity;
import advisor.nutrition.nutritionadvisor.ui.loaders.UserDayFoodsLoader;
import advisor.nutrition.nutritionadvisor.ui.loaders.UserDayLoader;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import timber.log.Timber;

public class NutritionCalculatorActivity extends AppCompatActivity {

    public static final String EXTRA_USER_NAME = "Actions.Extra.NutrtionCalculator.user_name";
    public static final String EXTRA_DATE = "Actions.Extra.NutrtionCalculator.date";

    @BindView(R.id.rv_foods)
    RecyclerView recyclerViewFoods;
    private FoodsAdapter foodsAdapter;

    @BindView(R.id.et_carbs)
    EditText editTextCarbs;
    @BindView(R.id.et_fat)
    EditText editTextFat;
    @BindView(R.id.et_prot)
    EditText editTextProteins;
    @BindView(R.id.tv_carbs_calculated)
    TextView textViewCarbsCalculated;
    @BindView(R.id.tv_fat_calculated)
    TextView textViewFatCalculated;
    @BindView(R.id.tv_proteins_calculated)
    TextView textViewProteinsCalculated;

    private Day mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_calculator);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String userName = "";
        String date = "";
        if(intent.hasExtra(EXTRA_USER_NAME) && intent.hasExtra(EXTRA_DATE)){
            userName = intent.getStringExtra(EXTRA_USER_NAME);
            date = intent.getStringExtra(EXTRA_DATE);
            Timber.d("open nutrition calculator for user %s and date %s", userName, date);
        } else {
            Timber.e("no extras defined");
            Toast.makeText(this, "Internal Error occurred", Toast.LENGTH_LONG).show();
        }

        mDay = new Day();
        mDay.setUserName(userName);
        mDay.setDate(date);
        mDay.setFoodList(new LinkedList<Food>());

        getSupportActionBar().setTitle(userName + " " + date);

        getSupportLoaderManager().restartLoader(UserDayFoodsLoader.LOADER_ID, null, new ThisUserDayFoodsLoader(this, date, userName));
        getSupportLoaderManager().restartLoader(UserDayLoader.LOADER_ID, null, new ThisUserDayLoader(this, date, userName));

        recyclerViewFoods.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.gv_food_column_count), LinearLayoutManager.VERTICAL, false));
        foodsAdapter = new FoodsAdapter(this);
        recyclerViewFoods.setAdapter(foodsAdapter);
    }

    private class ThisUserDayFoodsLoader extends UserDayFoodsLoader {
        ThisUserDayFoodsLoader(Context mContext, String mDate, String mUsername) {
            super(mContext, mDate, mUsername);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            updateDay(mDay, data);
            foodsAdapter.updateDay(mDay);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            foodsAdapter.updateDay(null);
        }
    }

    private class ThisUserDayLoader extends UserDayLoader {

        ThisUserDayLoader(Context mContext, String mDate, String mUsername) {
            super(mContext, mDate, mUsername);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null && data.moveToFirst()) {
                Timber.d("successfully loaded target-/ calculated nutritions");
                mDay.setTargetCarbs(data.getInt(data.getColumnIndex(UserDayColumns.TARGET_CARBS)));
                mDay.setTargetFat(data.getInt(data.getColumnIndex(UserDayColumns.TARGET_FAT)));
                mDay.setTargetProteins(data.getInt(data.getColumnIndex(UserDayColumns.TARGET_PROTEINS)));
                mDay.setCalculatedCarbs(data.getInt(data.getColumnIndex(UserDayColumns.CALCULATED_CARBS)));
                mDay.setCalculatedFat(data.getInt(data.getColumnIndex(UserDayColumns.CALCULATED_FAT)));
                mDay.setCalculatedProteins(data.getInt(data.getColumnIndex(UserDayColumns.CALCULATED_PROTEINS)));

                updateTargetCalculatedNutritionsUi();
            }
            //create new entry if not existing
            else {
                ContentValues values = new ContentValues();
                values.put(UserDayColumns.USER_NAME, mDay.getUserName());
                values.put(UserDayColumns.DATE, mDay.getDate());
                values.put(UserDayColumns.CALCULATED_CARBS, 0);
                values.put(UserDayColumns.CALCULATED_FAT, 0);
                values.put(UserDayColumns.CALCULATED_PROTEINS, 0);
                values.put(UserDayColumns.TARGET_CARBS, 0);
                values.put(UserDayColumns.TARGET_FAT, 0);
                values.put(UserDayColumns.TARGET_PROTEINS, 0);
                getContentResolver().insert(NutritionAdvisorProvider.UserDay.USER_DAY, values);

                mDay.setTargetCarbs(0);
                mDay.setTargetFat(0);
                mDay.setTargetProteins(0);
                mDay.setCalculatedCarbs(0);
                mDay.setCalculatedFat(0);
                mDay.setCalculatedProteins(0);

                updateTargetCalculatedNutritionsUi();
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            //do nothing
        }
    }

    boolean nutritionUiUpdateFlag = false;
    private void updateTargetCalculatedNutritionsUi() {
        if (!nutritionUiUpdateFlag) {
            nutritionUiUpdateFlag = true;
            editTextCarbs.setText(String.valueOf(mDay.getTargetCarbs()));
            editTextFat.setText(String.valueOf(mDay.getTargetFat()));
            editTextProteins.setText(String.valueOf(mDay.getTargetProteins()));
            textViewCarbsCalculated.setText(String.valueOf(mDay.getCalculatedCarbs()));
            textViewFatCalculated.setText(String.valueOf(mDay.getCalculatedFat()));
            textViewProteinsCalculated.setText(String.valueOf(mDay.getCalculatedProteins()));
            nutritionUiUpdateFlag = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(UserDayFoodsLoader.LOADER_ID, null, new ThisUserDayFoodsLoader(this, mDay.getDate(), mDay.getUserName()));
    }

    @OnTextChanged(value = R.id.et_carbs, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void targetCarbsChanged(android.text.Editable ignored){
        if (!nutritionUiUpdateFlag) {
            mDay.setTargetCarbs(Integer.valueOf(editTextCarbs.getText().toString()));
            Timber.d("Value of target carbs changed to %d", mDay.getTargetCarbs());
            updateTargetNutritients(UserDayColumns.TARGET_CARBS , mDay.getTargetCarbs());
        }
    }

    @OnTextChanged(value = R.id.et_fat, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void targetFatChanged(android.text.Editable ignored){
        if (!nutritionUiUpdateFlag) {
            mDay.setTargetFat(Integer.valueOf(editTextFat.getText().toString()));
            Timber.d("Value of target fat changed to %d", mDay.getTargetFat());
            updateTargetNutritients(UserDayColumns.TARGET_FAT , mDay.getTargetFat());
        }
    }

    @OnTextChanged(value = R.id.et_prot, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void targetProteinsChanged(android.text.Editable ignored){
        if (!nutritionUiUpdateFlag) {
            mDay.setTargetProteins(Integer.valueOf(editTextProteins.getText().toString()));
            Timber.d("Value of target proteins changed to %d", mDay.getTargetProteins());
            updateTargetNutritients(UserDayColumns.TARGET_PROTEINS , mDay.getTargetProteins());
        }
    }

    private void updateTargetNutritients(String column, int value) {
        ContentValues values = new ContentValues();
        values.put(column, value);
        getContentResolver().update(NutritionAdvisorProvider.UserDay.withUserAndDate(mDay.getUserName(), mDay.getDate()), values, null, null);
    }

    @OnClick(R.id.fab_add_food)
    public void addFoodFABClicked(){
        final Intent startAddFoodIntent = new Intent(this, AddFoodActivity.class);
        startAddFoodIntent.putExtra(AddFoodActivity.EXTRA_USER_NAME, mDay.getUserName());
        startAddFoodIntent.putExtra(AddFoodActivity.EXTRA_DATE, mDay.getDate());
        startActivity(startAddFoodIntent);
    }
}
