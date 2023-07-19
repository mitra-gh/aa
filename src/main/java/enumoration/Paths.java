package enumoration;

import java.util.Objects;

public enum Paths {
    USERS_PATH("files/user/users.json"),
    CURRENT_USER_PATH("files/user/currentUser.json"),
    USER_AVATARS("files/img/avatars/"),
    ;
    private final String path;

    Paths(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
