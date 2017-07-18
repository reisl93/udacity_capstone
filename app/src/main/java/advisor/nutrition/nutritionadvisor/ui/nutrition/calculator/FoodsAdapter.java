package advisor.nutrition.nutritionadvisor.ui.nutrition.calculator;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import advisor.nutrition.nutritionadvisor.R;
import advisor.nutrition.nutritionadvisor.data.Day;
import advisor.nutrition.nutritionadvisor.data.Food;
import advisor.nutrition.nutritionadvisor.provider.NutritionAdvisorProvider;
import advisor.nutrition.nutritionadvisor.provider.UserDayFoodColumns;
import advisor.nutrition.nutritionadvisor.ui.UiUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import timber.log.Timber;

class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.FoodViewHolder> {

    private final Context mContext;
    private final ValueChanged mCallback;
    private Day mDay;

    FoodsAdapter(Context mContext, ValueChanged callback) {
        this.mContext = mContext;
        this.mCallback = callback;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int viewType) {
        return new FoodViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.recycler_view_item_food, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        holder.bind(position);
    }

    /**
     * since we have a reference to the mDay, a change may happen without notice.
     * Therefore this method has to be called.
     */
    public void refresh(){
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDay == null ? 0 : mDay.getFoodList().size();
    }

    void updateDay(Day day) {
        this.mDay = day;
        Timber.d("updated cursor with length %d", getItemCount());
        refresh();
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {

        //no content provider updates while on #bind() calls.
        boolean textUpdateFlag = false;

        @BindView(R.id.tv_food_name)
        TextView textViewFoodName;
        @BindView(R.id.tv_carbs)
        TextView textViewCarbs;
        @BindView(R.id.tv_fat)
        TextView textViewFat;
        @BindView(R.id.tv_proteins)
        TextView textViewProteins;
        @BindView(R.id.tv_measure_target)
        TextView textViewMeasureTarget;
        @BindView(R.id.et_portions)
        EditText editTextPortion;

        @BindView(R.id.tv_calculated)
        TextView textViewCalculated;
        @BindView(R.id.tv_carbs_calculated)
        TextView textViewCarbsCalculated;
        @BindView(R.id.tv_fat_calculated)
        TextView textViewFatCalculated;
        @BindView(R.id.tv_proteins_calculated)
        TextView textViewProteinsCalculated;
        @BindView(R.id.tv_measure_calculated)
        TextView textViewMeasureCalculated;
        @BindView(R.id.tv_portions_calculated)
        TextView textViewPortionsCalculated;


        FoodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("DefaultLocale")
        void bind(int position) {
            if (mDay != null && !textUpdateFlag) {
                try {
                    textUpdateFlag = true;
                    Food food = mDay.getFoodList().get(position);
                    double portionSize = food.getPortionSize();
                    textViewFoodName.setText(food.getName());
                    ;
                    textViewMeasureTarget.setText(food.getMeasure());
                    textViewFat.setText(String.format("%.1f", food.getFat() / portionSize * food.getPreferencePortions()));
                    textViewCarbs.setText(String.format("%.1f", food.getCarbs() / portionSize * food.getPreferencePortions()));
                    textViewProteins.setText(String.format("%.1f", food.getProteins() / portionSize * food.getPreferencePortions()));
                    editTextPortion.setText(String.format("%.1f", food.getPreferencePortions() ));

                    if (food.getCalculatedPortions() != 0){
                        UiUtils.setVisibility(View.VISIBLE, textViewCarbsCalculated, textViewFatCalculated,
                                textViewProteinsCalculated, textViewMeasureCalculated,
                                textViewCalculated, textViewPortionsCalculated);
                        textViewCarbsCalculated.setText(String.format("%.1f", food.getCarbs() / portionSize * food.getCalculatedPortions()));
                        textViewFatCalculated.setText(String.format("%1$.1f", food.getFat() / portionSize * food.getCalculatedPortions()));
                        textViewProteinsCalculated.setText(String.format("%1$.1f",food.getProteins() / portionSize * food.getCalculatedPortions()));
                        textViewMeasureCalculated.setText(food.getMeasure());
                        textViewPortionsCalculated.setText(String.valueOf(food.getCalculatedPortions()));
                    } else {
                        UiUtils.setVisibility(View.GONE, textViewCarbsCalculated, textViewFatCalculated,
                                textViewProteinsCalculated, textViewMeasureCalculated,
                                textViewCalculated, textViewPortionsCalculated);
                    }
                } finally {
                    textUpdateFlag = false;
                }
            }
        }

        @OnClick(R.id.iv_add_portion)
        public void addPortion() {
            addToTargetPortions(25);
        }

        @OnClick(R.id.iv_subtract_portion)
        public void subtractPortion() {
            addToTargetPortions(-25);
        }

        @OnTextChanged(value = R.id.et_portions, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
        public void portionsChanged() {
            Food food = mDay.getFoodList().get(getAdapterPosition());
            food.setPreferencePortions(Double.valueOf(editTextPortion.getText().toString()));
            bind(getAdapterPosition());

            updateContentResolver(food);
        }

        private void addToTargetPortions(double portion) {
            Food food = mDay.getFoodList().get(getAdapterPosition());
            food.setPreferencePortions(Math.max(food.getPreferencePortions() + portion, 0 /* not below 0 */));
            bind(getAdapterPosition());

            updateContentResolver(food);
        }

        private void updateContentResolver(Food food) {
            if (!textUpdateFlag) {
                Timber.d("updating preference portion of food %d to %f", food.getId(), food.getPreferencePortions());
                ContentValues newTargetPortion = new ContentValues();
                newTargetPortion.put(UserDayFoodColumns.PREFERENCE_PORTIONS, food.getPreferencePortions());

                mContext.getContentResolver().update(NutritionAdvisorProvider.UserDayFood
                                .withUserAndDateAndFoodId(mDay.getUserName(), mDay.getDate(), food.getId()),
                        newTargetPortion, null, null);

                mCallback.valueChanged();
            }
        }

    }
}
