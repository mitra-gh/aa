package enumoration;

public enum Music {
    CLOWN("clown","/music/bg/Clown.mp3"),
    LOBBY_TIME("lobby-time","/music/bg/Lobby-Time.mp3"),
    MOJ_GARD("moj-gard","/music/bg/MOJ GRAD.mp3"),
    SHAKE("shake","/music/bg/Shake.mp3"),
    SNEAKY_SNITCH("sneaky-snitch","/music/bg/Sneaky-Snitch.mp3"),
    WALLPAPER("wallpaper","/music/bg/Wallpaper.mp3");
    private final String name;
    private final String path;

    Music(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
