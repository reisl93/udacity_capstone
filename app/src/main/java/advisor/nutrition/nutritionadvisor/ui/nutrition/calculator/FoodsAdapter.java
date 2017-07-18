package advisor.nutrition.nutritionadvisor.ui.nutrition.calculator;

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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import timber.log.Timber;

class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.FoodViewHolder> {

    private final Context mContext;
    private Day mDay;

    FoodsAdapter(Context mContext) {
        this.mContext = mContext;
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

    @Override
    public int getItemCount() {
        return mDay == null ? 0 : mDay.getFoodList().size();
    }

    void updateDay(Day day) {
        this.mDay = day;
        Timber.d("updated cursor with length %d", getItemCount());
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
        @BindView(R.id.et_portions)
        EditText editTextPortion;
        @BindView(R.id.tv_portion_size)
        TextView textViewPortionSize;

        FoodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int position) {
            if (mDay != null && !textUpdateFlag) {
                textUpdateFlag = true;
                Food food = mDay.getFoodList().get(position);
                textViewFoodName.setText(food.getName());
                textViewPortionSize.setText(mContext.getString(R.string.portion_size_s_s,
                        String.valueOf(food.getPortionSize()).replace("\\.0*", ""),
                        food.getMeasure()));
                textViewFat.setText(String.valueOf(food.getFat()));
                textViewCarbs.setText(String.valueOf(food.getCarbs()));
                textViewProteins.setText(String.valueOf(food.getProteins()));
                editTextPortion.setText(String.valueOf(food.getTargetPortions()));
                textUpdateFlag = false;
            }
        }

        @OnClick(R.id.iv_add_portion)
        public void addPortion() {
            addToTargetPortions(25);
        }

        @OnClick(R.id.iv_subtract_portion)
        public void subtractPortion() {
            addToTargetPortions(25);
        }

        @OnTextChanged(value = R.id.et_portions, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
        public void portionsChanged() {
            Food food = mDay.getFoodList().get(getAdapterPosition());
            food.setTargetPortions(Double.valueOf(editTextPortion.getText().toString()));
            bind(getAdapterPosition());

            updateContentResolver(food);
        }

        private void addToTargetPortions(double portion) {
            Food food = mDay.getFoodList().get(getAdapterPosition());
            food.setTargetPortions(Math.max(food.getTargetPortions() + portion, 0 /* not below 0 */));
            bind(getAdapterPosition());
            Timber.d("updating portion of food %d to %f", food.getId(), food.getTargetPortions());
            updateContentResolver(food);
        }

        private void updateContentResolver(Food food) {
            if (!textUpdateFlag) {
                ContentValues newTargetPortion = new ContentValues();
                newTargetPortion.put(UserDayFoodColumns.TARGET_PORTIONS, food.getTargetPortions());

                mContext.getContentResolver().update(NutritionAdvisorProvider.UserDayFood
                                .withUserAndDateAndFoodId(mDay.getUserName(), mDay.getDate(), food.getId()),
                        newTargetPortion, null, null);
            }
        }

    }
}
