package one.hyro.spark.proxy;

public class BanInfo {

    private final String reason;
    private final boolean permanent;
    private final String expiryDate; // Could be null for permanent bans

    public BanInfo(String reason, boolean permanent, String expiryDate) {
        this.reason = reason;
        this.permanent = permanent;
        this.expiryDate = expiryDate;
    }

    public String getReason() {
        return reason;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}
