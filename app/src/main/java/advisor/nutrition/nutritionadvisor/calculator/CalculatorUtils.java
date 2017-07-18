package advisor.nutrition.nutritionadvisor.calculator;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.util.List;

import javax.annotation.Nullable;

import advisor.nutrition.nutritionadvisor.data.Food;

class CalculatorUtils {

    static Food findFood(final List<Food> foodList, final int id){
        Optional<Food> foodOptional = Iterators.tryFind(foodList.iterator(), new Predicate<Food>() {
            @Override
            public boolean apply(@Nullable Food input) {
                return input != null && input.getId() == id;
            }
        });
        return foodOptional.orNull();
    }

    static double getSumCarbs(final List<Food> foodList){
        double carbs = 0;
        for (final Food food : foodList){
            carbs += (food.getCarbs() / food.getPortionSize()) * food.getCalculatedPortions();
        }
        return carbs;
    }

    static double getSumFat(final List<Food> foodList){
        double fat = 0;
        for (final Food food : foodList){
            fat += (food.getFat() / food.getPortionSize()) * food.getCalculatedPortions();
        }
        return fat;
    }

    static double getSumProteins(final List<Food> foodList){
        double proteins = 0;
        for (final Food food : foodList){
            proteins += (food.getProteins() / food.getPortionSize()) * food.getCalculatedPortions();
        }
        return proteins;
    }
}
