package advisor.nutrition.nutritionadvisor.ui.add.user;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import advisor.nutrition.nutritionadvisor.R;
import advisor.nutrition.nutritionadvisor.provider.NutritionAdvisorProvider;
import advisor.nutrition.nutritionadvisor.provider.UserColumns;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class AddUserActivity extends AppCompatActivity {

    @BindView(R.id.et_user_name)
    EditText userNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.b_add_user)
    public void addUserClicked(){
        String userName = userNameEditText.getText().toString().trim();
        if(!"".equals(userName)){
            ContentValues newUser = new ContentValues();
            newUser.put(UserColumns.NAME, userName);
            getContentResolver().insert(NutritionAdvisorProvider.Users.USERS, newUser);
            Timber.i("user %s created", userName);
            finish();
        } else {
            Toast.makeText(this, getString(R.string.no_user_added), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @OnClick(R.id.b_cancel)
    public void cancelClicked(){
        finish();
    }
}
