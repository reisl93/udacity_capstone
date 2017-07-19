package advisor.nutrition.nutritionadvisor.ui;

import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UiUtils {

    public static void setVisibility(final int visibility, View... views){
        for (final View view : views){
            view.setVisibility(visibility);
        }
    }

    public static String getDateString(int year, int month, int dayOfMonth) {
        Calendar day = Calendar.getInstance();
        day.set(Calendar.DAY_OF_YEAR, year);
        day.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        day.set(Calendar.MONTH, month);
        return new SimpleDateFormat("dd.MM.yyyy").format(day.getTime());
    }

    public static String getTodayString(){
        Calendar instance = Calendar.getInstance();
        return getDateString(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH));
    }
}
