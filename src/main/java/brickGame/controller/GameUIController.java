package brickGame.controller;

import brickGame.Main;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class GameUIController {

    // Display the score with animation at the specified (x, y) location.
    public void show(double x, double y, int score, Main main) {
        String sign = (score >= 0) ? "+" : "";
        Label label = new Label(sign + score);
        label.getStyleClass().add("game-message");
        label.setTranslateX(x);
        label.setTranslateY(y);

        Platform.runLater(() -> main.root.getChildren().add(label));

        animateLabel(label, main);
    }

    // Display a message with animation at a fixed position.
    public void showMessage(String message, Main main) {
        final Label label = new Label(message);
        label.getStyleClass().add("game-message");
        label.setTranslateX(220);
        label.setTranslateY(340);

        Platform.runLater(() -> main.root.getChildren().add(label));

        animateLabel(label, main);
    }

    //this method combines both opacity (fade-in/fade-out) and scaling animations to create a visually pleasing effect for the Label, and it handles the removal of the label after the animation is complete
    private void animateLabel(Label label, Main main) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), label);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), label);

        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);

        scaleTransition.setToX(5);
        scaleTransition.setToY(5);

        SequentialTransition sequentialTransition = new SequentialTransition(scaleTransition, fadeTransition);
        sequentialTransition.setOnFinished(event -> Platform.runLater(() -> main.root.getChildren().remove((label))));
        sequentialTransition.play();
    }
}
