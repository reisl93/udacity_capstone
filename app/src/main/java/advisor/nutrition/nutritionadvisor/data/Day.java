package advisor.nutrition.nutritionadvisor.data;

import java.util.LinkedList;
import java.util.List;

public class Day {
    private String date;
    private String userName;
    private List<Food> foodList;

    private int targetProteins;
    private int targetFat;
    private int targetCarbs;

    private int calculatedProteins;
    private int calculatedFat;
    private int calculatedCarbs;

    public Day(){}

    public Day(String date, String userName, List<Food> foodList, int targetProteins, int targetFat, int targetCarbs, int calculatedProteins, int calculatedFat, int calculatedCarbs) {
        this.date = date;
        this.userName = userName;
        this.foodList = foodList;
        this.targetProteins = targetProteins;
        this.targetFat = targetFat;
        this.targetCarbs = targetCarbs;
        this.calculatedProteins = calculatedProteins;
        this.calculatedFat = calculatedFat;
        this.calculatedCarbs = calculatedCarbs;
    }

    public int getTargetProteins() {
        return targetProteins;
    }

    public void setTargetProteins(int targetProteins) {
        this.targetProteins = targetProteins;
    }

    public int getTargetFat() {
        return targetFat;
    }

    public void setTargetFat(int targetFat) {
        this.targetFat = targetFat;
    }

    public int getTargetCarbs() {
        return targetCarbs;
    }

    public void setTargetCarbs(int targetCarbs) {
        this.targetCarbs = targetCarbs;
    }

    public int getCalculatedProteins() {
        return calculatedProteins;
    }

    public void setCalculatedProteins(int calculatedProteins) {
        this.calculatedProteins = calculatedProteins;
    }

    public int getCalculatedFat() {
        return calculatedFat;
    }

    public void setCalculatedFat(int calculatedFat) {
        this.calculatedFat = calculatedFat;
    }

    public int getCalculatedCarbs() {
        return calculatedCarbs;
    }

    public void setCalculatedCarbs(int calculatedCarbs) {
        this.calculatedCarbs = calculatedCarbs;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    public Day copy() {
        List<Food> foods = new LinkedList<>();
        for (final Food food : foodList) {
            foods.add(food.copy());
        }
        return new Day(date, userName, foods, targetProteins, targetFat, targetCarbs, calculatedProteins, calculatedFat, calculatedCarbs);
    }
}
