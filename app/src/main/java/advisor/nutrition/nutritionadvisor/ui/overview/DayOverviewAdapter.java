package advisor.nutrition.nutritionadvisor.ui.overview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import advisor.nutrition.nutritionadvisor.R;
import advisor.nutrition.nutritionadvisor.data.Day;
import advisor.nutrition.nutritionadvisor.ui.UiUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

class DayOverviewAdapter extends RecyclerView.Adapter<DayOverviewAdapter.DayViewHolder>{

    private final DaySelectedCallback mDayCallback;
    private Map<String, Day> mStringToDayMapping;

    private final Context mContext;

    DayOverviewAdapter(Context mContext, DaySelectedCallback dayCallback) {
        this.mContext = mContext;
        this.mDayCallback = dayCallback;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DayViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_day_overview, parent, false));
    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        holder.bind(position);
    }

    public int getItemCount() {
        return 100; //maximum 100 days in advance
    }


    class DayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_add_day)
        ImageView mAddDayButton;
        @BindView(R.id.iv_view_day)
        ImageView mViewDayButton;
        @BindView(R.id.tv_carbs)
        TextView mCarbsTextView;
        @BindView(R.id.tv_fat)
        TextView mFatTextView;
        @BindView(R.id.tv_proteins)
        TextView mProteinsTextView;
        @BindView(R.id.tv_date_title)
        TextView mDateTitleTextView;

        private String mPositionDay;

        DayViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(final int position){
            Calendar day = Calendar.getInstance();
            day.add(Calendar.DAY_OF_YEAR, position);
            mPositionDay = new SimpleDateFormat("dd.MM.yyyy").format(day.getTime());
            if (mStringToDayMapping != null && mStringToDayMapping.containsKey(mPositionDay)){
                bindDayExisting(mStringToDayMapping.get(mPositionDay));
            } else {
                bindDayEmpty();
            }
        }

        void bindDayEmpty(){
            UiUtils.setVisibility(View.VISIBLE, mAddDayButton);
            UiUtils.setVisibility(View.INVISIBLE, mViewDayButton, mFatTextView, mProteinsTextView, mCarbsTextView);
            mDateTitleTextView.setText(mPositionDay);
        }

        void bindDayExisting(final Day day){
            UiUtils.setVisibility(View.GONE, mAddDayButton);
            UiUtils.setVisibility(View.VISIBLE, mViewDayButton, mFatTextView, mProteinsTextView, mCarbsTextView);
            mDateTitleTextView.setText(mPositionDay);
            mCarbsTextView.setText(
                    String.format(mContext.getString(R.string.carbs_number),
                    day.getSumCarbs()));
            mFatTextView.setText(
                    String.format(mContext.getString(R.string.fat_number),
                            day.getSumFat()));
            mProteinsTextView.setText(
                    String.format(mContext.getString(R.string.prot_number),
                            day.getSumProteins()));
        }

        @Override
        public void onClick(View v) {
            mDayCallback.dayClicked(mPositionDay);
        }
    }
}
