package advisor.nutrition.nutritionadvisor.data;

public class Food {
    private int id;
    private double preferencePortions;
    private double calculatedPortions;
    private double portionSize;
    private String measure;
    private double carbs;
    private double fat;
    private double proteins;
    private String name;

    public Food(){}

    public Food(int id, double preferencePortions, double calculatedPortions, double portionSize, String measure, double carbs, double fat, double proteins, String name) {
        this.id = id;
        this.preferencePortions = preferencePortions;
        this.calculatedPortions = calculatedPortions;
        this.portionSize = portionSize;
        this.measure = measure;
        this.carbs = carbs;
        this.fat = fat;
        this.proteins = proteins;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPreferencePortions() {
        return preferencePortions;
    }

    public void setPreferencePortions(double preferencePortions) {
        this.preferencePortions = preferencePortions;
    }

    public double getPortionSize() {
        return portionSize;
    }

    public void setPortionSize(double portionSize) {
        this.portionSize = portionSize;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getProteins() {
        return proteins;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Food)) return false;

        Food food = (Food) o;

        if (id != food.id) return false;
        if (Double.compare(food.preferencePortions, preferencePortions) != 0) return false;
        if (Double.compare(food.portionSize, portionSize) != 0) return false;
        if (Double.compare(food.carbs, carbs) != 0) return false;
        if (Double.compare(food.fat, fat) != 0) return false;
        if (Double.compare(food.proteins, proteins) != 0) return false;
        if (!measure.equals(food.measure)) return false;
        return name.equals(food.name);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        temp = Double.doubleToLongBits(preferencePortions);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(portionSize);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + measure.hashCode();
        temp = Double.doubleToLongBits(carbs);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(fat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(proteins);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Food{" + "id=" + id + ", preferencePortions=" + preferencePortions + ", portionSize=" + portionSize + ", measure='" + measure + '\'' + ", carbs=" + carbs + ", fat=" + fat + ", proteins=" + proteins + ", name='" + name + '\'' + '}';
    }

    public double getCalculatedPortions() {
        return calculatedPortions;
    }

    public void setCalculatedPortions(double calculatedPortions) {
        this.calculatedPortions = calculatedPortions;
    }

    public Food copy(){
        return new Food(id, preferencePortions, calculatedPortions, portionSize, measure, carbs, fat, proteins, name);
    }
}
