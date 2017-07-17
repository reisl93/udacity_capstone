package advisor.nutrition.nutritionadvisor.ui.nutrition.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import advisor.nutrition.nutritionadvisor.R;

public class NutritionCalculatorActivity extends AppCompatActivity {

    public static final String EXTRA_USER_NAME = "Actions.Extra.NutrtionCalculator.user_name";
    public static final String EXTRA_DATE = "Actions.Extra.NutrtionCalculator.date";

    final


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_calculator);
    }
}
