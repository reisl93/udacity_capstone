package advisor.nutrition.nutritionadvisor.ui.nutrition.calculator;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import advisor.nutrition.nutritionadvisor.R;
import advisor.nutrition.nutritionadvisor.data.Food;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.FoodViewHolder> {

    private Cursor mFoodCursor;
    private final Context mContext;

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
        return mFoodCursor == null ? 0 : mFoodCursor.getCount();
    }

    void updateFoodCursor(Cursor foodCursor) {
        this.mFoodCursor = foodCursor;
        Timber.d("updated cursor with length %d", getItemCount());
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {

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
            if (mFoodCursor != null) {
                mFoodCursor.moveToPosition(position);
                Food food = NutritionCalculatorUtils.convertCursorTooFood(mFoodCursor);
                textViewFoodName.setText(food.getName());
                textViewPortionSize.setText(mContext.getString(R.string.portion_size_s_s,
                        String.valueOf(food.getPortionSize()).replace("\\.0*", ""),
                        food.getMeasure()));
                textViewFat.setText(String.valueOf(food.getFat()));
                textViewCarbs.setText(String.valueOf(food.getCarbs()));
                textViewProteins.setText(String.valueOf(food.getProteins()));
                editTextPortion.setText(String.valueOf(food.getPortions()));
            }

        }
    }
}
