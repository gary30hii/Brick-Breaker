module brickGame {
    requires javafx.fxml;
    requires javafx.controls;
    requires org.slf4j;
    requires java.desktop;
    requires jlayer;
    requires javafx.media;

    opens brickGame to javafx.fxml;
    exports brickGame;
    exports brickGame.model;
    opens brickGame.model to javafx.fxml;
    exports brickGame.engine;
    opens brickGame.engine to javafx.fxml;
    exports brickGame.controller;
    opens brickGame.controller to javafx.fxml;
}