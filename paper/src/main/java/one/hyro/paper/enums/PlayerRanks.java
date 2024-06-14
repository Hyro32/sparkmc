package one.hyro.paper.enums;

public enum PlayerRanks {
    OWNER("\uE000", 4),
    ADMINISTRATOR("\uE001", 3),
    MODERATOR("\uE002", 2),
    DEFAULT(null, 1);

    private final String unicode;
    private final int priority;

    PlayerRanks(String unicode, int priority) {
        this.unicode = unicode;
        this.priority = priority;
    }

    public String getUnicode() {
        return unicode;
    }

    public int getPriority() {
        return priority;
    }
}
