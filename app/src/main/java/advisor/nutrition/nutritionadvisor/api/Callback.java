package advisor.nutrition.nutritionadvisor.api;

public interface Callback<T> {
    void response(final T response);
}
