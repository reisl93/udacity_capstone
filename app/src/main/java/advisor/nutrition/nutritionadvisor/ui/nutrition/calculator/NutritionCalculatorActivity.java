package advisor.nutrition.nutritionadvisor.ui.nutrition.calculator;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.LinkedList;

import advisor.nutrition.nutritionadvisor.NutritionAdvisorApp;
import advisor.nutrition.nutritionadvisor.R;
import advisor.nutrition.nutritionadvisor.calculator.CalculateForDay;
import advisor.nutrition.nutritionadvisor.data.Day;
import advisor.nutrition.nutritionadvisor.data.Food;
import advisor.nutrition.nutritionadvisor.provider.DataProviderUtils;
import advisor.nutrition.nutritionadvisor.provider.NutritionAdvisorProvider;
import advisor.nutrition.nutritionadvisor.provider.UserDayColumns;
import advisor.nutrition.nutritionadvisor.ui.UiUtils;
import advisor.nutrition.nutritionadvisor.ui.add.food.AddFoodActivity;
import advisor.nutrition.nutritionadvisor.ui.food.list.FoodListActivity;
import advisor.nutrition.nutritionadvisor.ui.loaders.UserDayFoodsLoader;
import advisor.nutrition.nutritionadvisor.ui.loaders.UserDayLoader;
import advisor.nutrition.nutritionadvisor.ui.widget.FoodListUpdateService;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import timber.log.Timber;

public class NutritionCalculatorActivity extends AppCompatActivity implements ValueChanged{

    public static final String EXTRA_USER_NAME = "Actions.Extra.NutritionCalculator.user_name";
    public static final String EXTRA_DATE = "Actions.Extra.NutritionCalculator.date";

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

    @BindView(R.id.ib_show_list)
    ImageButton imageButtonShowFoodList;

    private Day mDay;
    private AsyncTask<Void, Void, Void> mAasyncCalculatorTask;


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
        } else if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_USER_NAME) && savedInstanceState.containsKey(EXTRA_DATE)) {
            userName = savedInstanceState.getString(EXTRA_USER_NAME);
            date = savedInstanceState.getString(EXTRA_DATE);
            Timber.d("open nutrition calculator for user %s and date %s", userName, date);
        } else {
            finish();
            Timber.e("no extras defined");
        }

        mDay = new Day();
        mDay.setUserName(userName);
        mDay.setDate(date);
        mDay.setFoodList(new LinkedList<Food>());

        getSupportActionBar().setTitle(userName + " " + date);

        getSupportLoaderManager().restartLoader(UserDayFoodsLoader.LOADER_ID, null, new ThisUserDayFoodsLoader(this, date, userName));
        getSupportLoaderManager().restartLoader(UserDayLoader.LOADER_ID, null, new ThisUserDayLoader(this, date, userName));

        recyclerViewFoods.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.gv_food_column_count), LinearLayoutManager.VERTICAL, false));
        foodsAdapter = new FoodsAdapter(this, this);
        recyclerViewFoods.setAdapter(foodsAdapter);

        Tracker tracker = ((NutritionAdvisorApp) getApplication()).getDefaultTracker();
        tracker.setScreenName(AddFoodActivity.class.getSimpleName());
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(this).dispatchLocalHits();

        updateCaluclatedNutritionUI();

        getSupportActionBar().setElevation(0);
    }

    @Override
    public void valueChanged() {
        recalculatePortions();
    }

    private void recalculatePortions() {
        Timber.d("valueChanged - entry");
        if(mAasyncCalculatorTask != null){
            Timber.d("valueChanged - canceling old task");
            mAasyncCalculatorTask.cancel(true);
            updateCaluclatedNutritionUI();
        }
        if (mDay != null && (mDay.getTargetCarbs() > 0 || mDay.getTargetFat() > 0 || mDay.getTargetProteins() > 0)
                && mDay.getFoodList().size() > 0) {
            mAasyncCalculatorTask = new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    new CalculateForDay(NutritionCalculatorActivity.this, mDay).startCalculating();
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    updateTargetCalculatedNutritionsUi();
                }
            };
            mAasyncCalculatorTask.execute();
        }
    }

    private void updateCaluclatedNutritionUI() {
        if (mDay.getCalculatedCarbs() > 0 || mDay.getCalculatedFat() > 0 || mDay.getCalculatedProteins() > 0) {
            textViewCarbsCalculated.setText(String.valueOf(mDay.getCalculatedCarbs()));
            textViewFatCalculated.setText(String.valueOf(mDay.getCalculatedFat()));
            textViewProteinsCalculated.setText(String.valueOf(mDay.getCalculatedProteins()));
        }

        if (mDay.getFoodList().size() > 0){
            imageButtonShowFoodList.setVisibility(View.VISIBLE);
            Timber.d("making button visible");
        } else {
            imageButtonShowFoodList.setVisibility(View.INVISIBLE);
            Timber.d("making button invisible");
        }

        foodsAdapter.refresh();
    }

    private class ThisUserDayFoodsLoader extends UserDayFoodsLoader {
        ThisUserDayFoodsLoader(Context mContext, String mDate, String mUsername) {
            super(mContext, mDate, mUsername);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            updateDay(mDay, data);
            foodsAdapter.updateDay(mDay);
            updateTargetCalculatedNutritionsUi();
            updateCaluclatedNutritionUI();
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
                Timber.d("writing to db: %s", values.toString());
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
        Timber.i("resume nutrition caluclator with %s %s", mDay.getDate(), mDay.getUserName());
        getSupportLoaderManager().restartLoader(UserDayFoodsLoader.LOADER_ID, null, new ThisUserDayFoodsLoader(this, mDay.getDate(), mDay.getUserName()));
    }

    @OnTextChanged(value = R.id.et_carbs, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void targetCarbsChanged(android.text.Editable ignored){
        if (!nutritionUiUpdateFlag) {
            mDay.setTargetCarbs(Integer.valueOf(editTextCarbs.getText().toString()));
            Timber.d("Value of target carbs changed to %d", mDay.getTargetCarbs());
            updateTargetNutritients(UserDayColumns.TARGET_CARBS , mDay.getTargetCarbs());
            recalculatePortions();
        }
    }

    @OnTextChanged(value = R.id.et_fat, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void targetFatChanged(android.text.Editable ignored){
        if (!nutritionUiUpdateFlag) {
            mDay.setTargetFat(Integer.valueOf(editTextFat.getText().toString()));
            Timber.d("Value of target fat changed to %d", mDay.getTargetFat());
            updateTargetNutritients(UserDayColumns.TARGET_FAT , mDay.getTargetFat());
            recalculatePortions();
        }
    }

    @OnTextChanged(value = R.id.et_prot, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void targetProteinsChanged(android.text.Editable ignored){
        if (!nutritionUiUpdateFlag) {
            mDay.setTargetProteins(Integer.valueOf(editTextProteins.getText().toString()));
            Timber.d("Value of target proteins changed to %d", mDay.getTargetProteins());
            updateTargetNutritients(UserDayColumns.TARGET_PROTEINS , mDay.getTargetProteins());
            recalculatePortions();
        }
    }

    private void updateTargetNutritients(String column, int value) {
        ContentValues values = new ContentValues();
        values.put(column, value);
        getContentResolver().update(NutritionAdvisorProvider.UserDay.withUserAndDate(mDay.getUserName(), mDay.getDate()), values, null, null);
        Timber.i("DB-Table-UserDay: wrote to column %s the value %d", column, value );
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (UiUtils.getTodayString().equals(mDay.getDate())){
            Timber.i("updating the current widget food");
            DataProviderUtils.deleteWidgetFoodList(NutritionCalculatorActivity.this);
            DataProviderUtils.updateWidgetFoodList(NutritionCalculatorActivity.this, mDay);
            FoodListUpdateService.startActionUpdateFoodListWidgets(this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_DATE, mDay.getDate());
        outState.putString(EXTRA_USER_NAME, mDay.getUserName());
        super.onSaveInstanceState(outState);
    }

    @OnClick(R.id.ib_show_list)
    public void showListClicked(){
        final Intent startFoodListActivity = new Intent(this, FoodListActivity.class);
        startFoodListActivity.putExtra(FoodListActivity.EXTRA_USER_NAME, mDay.getUserName());
        startFoodListActivity.putExtra(FoodListActivity.EXTRA_DATE, mDay.getDate());
        startActivity(startFoodListActivity);
    }

    @OnClick(R.id.fab_add_food)
    public void addFoodFABClicked(){
        final Intent startAddFoodIntent = new Intent(this, AddFoodActivity.class);
        startAddFoodIntent.putExtra(AddFoodActivity.EXTRA_USER_NAME, mDay.getUserName());
        startAddFoodIntent.putExtra(AddFoodActivity.EXTRA_DATE, mDay.getDate());
        startActivity(startAddFoodIntent);
    }
}
