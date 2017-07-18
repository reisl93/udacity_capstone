package advisor.nutrition.nutritionadvisor.api.ndb.data;

import advisor.nutrition.nutritionadvisor.data.Food;

public class ConverterUtils {

    private final static String NDB_NUTRIENT_ID_PROTEIN = "203";
    private final static String NDB_NUTRIENT_ID_FAT = "204";
    private final static String NDB_NUTRIENT_ID_CARBS = "205";

    public static Food convertNdbFoodResponse(NdbFoodReportResponse reportResponse){
        final Food food = new Food();

        NdbFoodReportResponse.NdbFood ndbFood = reportResponse.getNdbReport().getFood();
        food.setId(Integer.valueOf(ndbFood.getNdbNumber()));
        food.setName(ndbFood.getName());

        NdbFoodReportResponse.NdbNutrients[] nutrients = ndbFood.getNutrients();
        for (NdbFoodReportResponse.NdbNutrients nutrient : nutrients){
            switch (nutrient.getNutrientId()){
                case NDB_NUTRIENT_ID_PROTEIN:
                    food.setProteins(Double.valueOf(nutrient.getValuePer100G()));
                    break;
                case NDB_NUTRIENT_ID_FAT:
                    food.setFat(Double.valueOf(nutrient.getValuePer100G()));
                    break;
                case NDB_NUTRIENT_ID_CARBS:
                    food.setCarbs(Double.valueOf(nutrient.getValuePer100G()));
                    break;
            }
        }

        //100g per default
        food.setPortionSize(100);
        food.setMeasure("g");

        return food;
    }
}
