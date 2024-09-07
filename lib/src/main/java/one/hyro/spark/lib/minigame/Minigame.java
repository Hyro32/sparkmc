package one.hyro.spark.lib.minigame;

public interface Minigame {
    void waiting(Session session);
    void starting(Session session);
    void playing(Session session);
    void ending(Session session);
}
