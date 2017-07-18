package advisor.nutrition.nutritionadvisor.ui.overview;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import advisor.nutrition.nutritionadvisor.R;
import advisor.nutrition.nutritionadvisor.ui.add.user.AddUserActivity;
import advisor.nutrition.nutritionadvisor.ui.nutrition.calculator.NutritionCalculatorActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class UserDayOverviewActivity extends AppCompatActivity implements DaySelectedCallback {

    @BindView(R.id.s_select_user)
    AppCompatSpinner mUserSpinner;
    private UserSpinnerAdapter userSpinnerAdapter ;

    @BindView(R.id.rv_calender)
    RecyclerView recyclerViewCalendar;

    private String[] mUsers = new String[0];
    private String mUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_day_overview);
        ButterKnife.bind(this);

        final DayOverviewAdapter adapter = new DayOverviewAdapter(this, this);
        int columnCount = getResources().getInteger(R.integer.gv_calendar_column_count);
        recyclerViewCalendar.setLayoutManager(
                new GridLayoutManager(this, columnCount, LinearLayoutManager.VERTICAL, false));
        recyclerViewCalendar.setAdapter(adapter);

        userSpinnerAdapter = new UserSpinnerAdapter(this);
        mUserSpinner.setOnItemSelectedListener(new UserNameSelectedListener());
        mUserSpinner.setAdapter(userSpinnerAdapter);
        refreshSpinner(new String[0]);
    }

    private void refreshSpinner(String[] users){
        mUsers = users;
        if (mUserName == null && users.length > 0){
            mUserName = users[0];
        }
        userSpinnerAdapter.clear();
        userSpinnerAdapter.addAll(users);
        Timber.d("reloading users with length %d", users.length);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(UsersLoader.LOADER_ID, null, new ThisUsersLoader(this));
    }

    @OnClick(R.id.iv_add_user)
    public void addUserClicked(){
        startActivity(new Intent(this, AddUserActivity.class));
    }

    @Override
    public void dayClicked(String date) {
        if (mUserName != null && date != null) {
            Intent nutritionCalculatorActivityIntent = new Intent(this, NutritionCalculatorActivity.class);
            nutritionCalculatorActivityIntent.putExtra(NutritionCalculatorActivity.EXTRA_USER_NAME, mUserName);
            nutritionCalculatorActivityIntent.putExtra(NutritionCalculatorActivity.EXTRA_DATE, date);
            startActivity(nutritionCalculatorActivityIntent);
        } else {
            Timber.i("no user selected, can't start food calculator");
            Toast.makeText(this, "no user selected", Toast.LENGTH_LONG).show();
        }
    }

    private final class ThisUsersLoader extends UsersLoader {

        ThisUsersLoader(Context mContext) {
            super(mContext);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            refreshSpinner(getUsers(data));
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            refreshSpinner(new String[0]);
        }
    }

    private class UserNameSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (mUsers.length < position){
                mUserName = mUsers[position];
                Timber.d("user %s selected", mUserName);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) { /* intentionally left void */
        }
    }
}
