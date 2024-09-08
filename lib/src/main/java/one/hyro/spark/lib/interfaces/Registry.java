package one.hyro.spark.lib.interfaces;

public interface Registry<T> {
    void register(T record);
    void unregister(T record);
}
