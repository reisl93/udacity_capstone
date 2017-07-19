package advisor.nutrition.nutritionadvisor.ui.food.list;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import advisor.nutrition.nutritionadvisor.R;
import advisor.nutrition.nutritionadvisor.data.Food;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.FoodViewHolder> {

    private List<Food> mFoodList;

    @Override
    public FoodViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int viewType) {
        return new FoodViewHolder(
                LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.recycler_view_item_food_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mFoodList == null ? 0 : mFoodList.size();
    }

    void updateFoodList(List<Food> foodList) {
        this.mFoodList = foodList;
        Timber.d("updated list with length %d", getItemCount());
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_food_name)
        TextView textViewFoodName;
        @BindView(R.id.tv_measure)
        TextView textViewMeasure;
        @BindView(R.id.tv_portion)
        TextView textViewPortion;

        FoodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int position) {
            if (mFoodList != null && mFoodList.size() > position){
                final Food food = mFoodList.get(position);
                textViewFoodName.setText(food.getName());
                textViewMeasure.setText(food.getMeasure());
                textViewPortion.setText(String.valueOf(food.getCalculatedPortions()));
            }
        }
    }
}
