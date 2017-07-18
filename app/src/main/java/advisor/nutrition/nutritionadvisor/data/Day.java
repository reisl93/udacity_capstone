package advisor.nutrition.nutritionadvisor.data;

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

    public List<Food> getFoodList(){
        return foodList;
    }
}
