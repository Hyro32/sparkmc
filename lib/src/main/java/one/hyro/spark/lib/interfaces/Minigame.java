package one.hyro.spark.lib.interfaces;

import one.hyro.spark.lib.session.Session;

public interface Minigame {
    void waiting(Session session);
    void starting(Session session);
    void playing(Session session);
    void ending(Session session);
}
