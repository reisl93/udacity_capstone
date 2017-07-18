package advisor.nutrition.nutritionadvisor.api.ndb.data;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings({"WeakerAccess", "unused"})
public class NdbFoodReportResponse {
    @SerializedName("report")
    public NdbReport ndbReport;

    public NdbReport getNdbReport() {
        return ndbReport;
    }

    @SerializedName("report")
    public void setNdbReport(NdbReport ndbReport) {
        this.ndbReport = ndbReport;
    }

    public static class NdbReport {
        public NdbFood food;

        public NdbFood getFood() {
            return food;
        }

        public void setFood(NdbFood food) {
            this.food = food;
        }
    }

    public static class NdbFood {
        public String name;
        @SerializedName("ndbno")
        public String ndbNumber;
        public NdbNutrients[] nutrients;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public NdbNutrients[] getNutrients() {
            return nutrients;
        }

        public void setNutrients(NdbNutrients[] nutrients) {
            this.nutrients = nutrients;
        }

        public String getNdbNumber() {
            return ndbNumber;
        }

        @SerializedName("ndbno")
        public void setNdbNumber(String ndbNumber) {
            this.ndbNumber = ndbNumber;
        }
    }

    public static class NdbNutrients {
        @SerializedName("nutrient_id")
        public String nutrientId;
        public String name;
        public String unit;
        @SerializedName("value")
        public String valuePer100G;

        public String getNutrientId() {
            return nutrientId;
        }

        @SerializedName("nutrient_id")
        public void setNutrientId(String nutrientId) {
            this.nutrientId = nutrientId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getValuePer100G() {
            return valuePer100G;
        }

        @SerializedName("value")
        public void setValuePer100G(String valuePer100G) {
            this.valuePer100G = valuePer100G;
        }
    }
}
