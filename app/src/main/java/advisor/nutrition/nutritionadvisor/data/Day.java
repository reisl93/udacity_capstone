package advisor.nutrition.nutritionadvisor.data;

import java.util.List;

public class Day {
    private String day;
    private int userId;
    private List<Food> foodList;



    public double getSumCarbs(){
        double carbs = 0;
        for (final Food food : foodList){
            carbs += food.getCarbs();
        }
        return carbs;
    }

    public double getSumFat(){
        double fat = 0;
        for (final Food food : foodList){
            fat += food.getFat();
        }
        return fat;
    }

    public double getSumProteins(){
        double proteins = 0;
        for (final Food food : foodList){
            proteins += food.getProteins();
        }
        return proteins;
    }
}
