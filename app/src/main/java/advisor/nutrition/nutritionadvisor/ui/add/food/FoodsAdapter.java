package advisor.nutrition.nutritionadvisor.ui.add.food;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import advisor.nutrition.nutritionadvisor.R;
import advisor.nutrition.nutritionadvisor.data.Food;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.FoodViewHolder> {

    private Food[] mFoods;
    private final Context mContext;
    private final FoodSelectedListener mFoodSelectedListener;

    FoodsAdapter(Context mContext, FoodSelectedListener foodSelectedListener) {
        this.mContext = mContext;
        mFoodSelectedListener = foodSelectedListener;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new FoodViewHolder(inflater.inflate(R.layout.recycler_view_item_food_search, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mFoods == null ? 0 : mFoods.length;
    }

    void updateFoods(Food[] foods) {
        Timber.d("updated foods");
        this.mFoods = foods;
    }

    class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tv_food_name)
        TextView textViewFoodName;
        @BindView(R.id.tv_carbs)
        TextView textViewCarbs;
        @BindView(R.id.tv_fat)
        TextView textViewFat;
        @BindView(R.id.tv_proteins)
        TextView textViewProteins;
        @BindView(R.id.tv_portion_size)
        TextView textViewPortionSize;

        FoodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            if (mFoods != null && position < mFoods.length ) {
                Food food = mFoods[position];
                if (food != null) {
                    textViewFoodName.setText(food.getName());
                    textViewPortionSize.setText(mContext.getString(R.string.portion_size_s_s,
                            String.valueOf(food.getPortionSize()).replace("\\.0*",""),
                            food.getMeasure()));
                    textViewFat.setText(String.valueOf(food.getFat()));
                    textViewCarbs.setText(String.valueOf(food.getCarbs()));
                    textViewProteins.setText(String.valueOf(food.getProteins()));
                } else {
                    Timber.e("the food on position %d is null", position);
                }
            } else {
                Timber.e("Couldn't update position %d", position);
            }
        }

        @Override
        public void onClick(View v) {
            mFoodSelectedListener.selected(mFoods[getAdapterPosition()]);
        }
    }
}
