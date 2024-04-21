package one.hyro.lib.services;

public enum ApiEndpoints {
    PLAYERS("http://localhost:3000/players"),
    SANCTIONS("http://localhost:3000/sanctions");

    private final String url;

    ApiEndpoints(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
