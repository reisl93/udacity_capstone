package advisor.nutrition.nutritionadvisor.api.ndb.data;


import com.google.gson.annotations.SerializedName;

@SuppressWarnings({"unused", "WeakerAccess"}) // for gson
public class NdbSearchResponse {
    public NdbList list;

    public NdbList getList() {
        return list;
    }

    public void setList(NdbList list) {
        this.list = list;
    }

    public static class NdbList {
        public FoodList[] item;

        public FoodList[] getItem() {
            return item;
        }

        public void setItem(FoodList[] item) {
            this.item = item;
        }
    }

    public static class FoodList {
        public String name;
        @SerializedName("ndbno")
        public String ndbNumber;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNdbNumber() {
            return ndbNumber;
        }

        @SerializedName("ndbno")
        public void setNdbNumber(String ndbNumber) {
            this.ndbNumber = ndbNumber;
        }
    }
}
