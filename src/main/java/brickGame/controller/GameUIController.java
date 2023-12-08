package brickGame.controller;

import brickGame.Main;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Controller class for game UI animations. It provides functionality to display animated labels for scores and messages.
 */
public class GameUIController {

    /**
     * Displays the score with an animation at a specified location on the screen.
     *
     * @param x     The X coordinate where the label should appear.
     * @param y     The Y coordinate where the label should appear.
     * @param score The score to display. Positive scores are prefixed with a plus sign.
     * @param main  The reference to the main application class, used to access the root pane for displaying the label.
     */
    public void show(double x, double y, int score, Main main) {
        String sign = (score >= 0) ? "+" : "";
        Label label = new Label(sign + score);
        label.getStyleClass().add("game-message");
        label.setTranslateX(x);
        label.setTranslateY(y);

        Platform.runLater(() -> main.getRoot().getChildren().add(label));

        animateLabel(label, main);
    }

    /**
     * Displays a message with an animation at a fixed central position on the screen.
     *
     * @param message The message to display in the label.
     * @param main    The reference to the main application class, used to access the root pane for displaying the label.
     */
    public void showMessage(String message, Main main) {
        final Label label = new Label(message);
        label.getStyleClass().add("game-message");
        label.setTranslateX(220);
        label.setTranslateY(340);

        Platform.runLater(() -> main.getRoot().getChildren().add(label));

        animateLabel(label, main);
    }

    /**
     * Animates a given label with a combination of scaling and fading transitions. The label is removed from the root pane after the animation completes.
     *
     * @param label The label to animate.
     * @param main  The reference to the main application class, used to access the root pane for removing the label after animation.
     */
    private void animateLabel(Label label, Main main) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), label);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), label);

        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);

        scaleTransition.setToX(5);
        scaleTransition.setToY(5);

        SequentialTransition sequentialTransition = new SequentialTransition(scaleTransition, fadeTransition);
        sequentialTransition.setOnFinished(event -> Platform.runLater(() -> main.getRoot().getChildren().remove((label))));
        sequentialTransition.play();
    }
}
