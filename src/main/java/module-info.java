module e {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.google.gson;
    requires java.desktop;
    requires java.logging;
    requires org.controlsfx.controls;
    requires org.apache.commons.codec;


    exports view;
    opens view to javafx.fxml;
    exports view.animations;
    opens view.animations to javafx.fxml;
    exports view.menus;
    opens view.menus to javafx.fxml;
    exports model.game;
    opens model.game to javafx.fxml;
    exports view.menus.profile;
    opens view.menus.profile to javafx.fxml;
    exports enumoration;
    opens enumoration to javafx.fxml;
    exports model;
    opens model to com.google.gson;
    exports model.other;
    opens model.other to com.google.gson;

}