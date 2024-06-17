package one.hyro.instances;

public interface Minigame {
    void waiting(GameSession session);
    void starting(GameSession session);
    void inGame(GameSession session);
    void ending(GameSession session);
}
