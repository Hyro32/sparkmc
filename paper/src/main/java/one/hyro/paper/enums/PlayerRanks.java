package one.hyro.paper.enums;

public enum PlayerRanks {
    OWNER("\uE001", 7),
    ADMINISTRATOR("\uE002", 6),
    MODERATOR("\uE003", 5),
    HELPER("\uE004", 4),
    MVP("\uE005", 3),
    VIP("\uE006", 2),
    DEFAULT("\uE007", 1);

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
