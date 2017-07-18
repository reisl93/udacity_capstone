package advisor.nutrition.nutritionadvisor.ui.overview;


import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.sch.calendar.adapter.VagueAdapter;

import java.util.Map;

import advisor.nutrition.nutritionadvisor.R;
import advisor.nutrition.nutritionadvisor.data.Day;
import advisor.nutrition.nutritionadvisor.ui.UiUtils;
import timber.log.Timber;

class CalendarAdapter extends VagueAdapter {

    private Map<String, Day> mStringToDayMapping;

    CalendarAdapter(@LayoutRes int dayLayout) {
        super(dayLayout);
    }

    @Override
    public void onBindVague(View itemView, int year, int month, int dayOfMonth) {
        String mPositionDay = UiUtils.getDateString(year, month, dayOfMonth);

        Timber.v("updating %d.%d.%d",year,month,dayOfMonth);

        View container = itemView.findViewById(R.id.nutrition_container);
        TextView carbs = (TextView) itemView.findViewById(R.id.tv_carbs);
        TextView fat = (TextView) itemView.findViewById(R.id.tv_fat);
        TextView proteins = (TextView) itemView.findViewById(R.id.tv_proteins);
        Day selectedDay = null;
        if (mStringToDayMapping != null && mStringToDayMapping.containsKey(mPositionDay)) {
            selectedDay = mStringToDayMapping.get(mPositionDay);
        }
        if (selectedDay != null &&
                (selectedDay.getTargetCarbs() > 0 || selectedDay.getTargetFat() > 0 || selectedDay.getTargetProteins() > 0)) {
            UiUtils.setVisibility(View.VISIBLE, container);
            carbs.setText(String.valueOf(selectedDay.getTargetCarbs()));
            fat.setText(String.valueOf(selectedDay.getTargetFat()));
            proteins.setText(String.valueOf(selectedDay.getTargetProteins()));
        } else {
            UiUtils.setVisibility(View.GONE, container);
        }
    }

    @Override
    public void flagToday(View todayView) {
        //intentionally void to override super
    }

    void setmStringToDayMapping(Map<String, Day> mStringToDayMapping) {
        Timber.d("updating map");
        this.mStringToDayMapping = mStringToDayMapping;
        notifyDataSetChanged();
    }
}
