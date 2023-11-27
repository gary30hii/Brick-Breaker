package brickGame;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

//import sun.plugin2.message.Message;

public class Score {

    // Display the score with animation at the specified (x, y) location.
    public void show(double x, double y, int score, Main main) {
        String sign = (score >= 0) ? "+" : "";
        Label label = new Label(sign + score);
        label.setTranslateX(x);
        label.setTranslateY(y);

        Platform.runLater(() -> main.root.getChildren().add(label));

        animateLabel(label, main);
    }

    // Display a message with animation at a fixed position.
    public void showMessage(String message, Main main) {
        final Label label = new Label(message);
        label.setTranslateX(220);
        label.setTranslateY(340);

        Platform.runLater(() -> main.root.getChildren().add(label));

        animateLabel(label, main);
    }

    // Display a "Game Over" message and an option to restart the game.
    public void showGameOver(final Main main) {
        Platform.runLater(() -> {
            Label label = new Label("Game Over :(");
            label.setTranslateX(200);
            label.setTranslateY(250);
            label.setScaleX(2);
            label.setScaleY(2);

            Button restart = new Button("Restart");
            restart.setTranslateX(220);
            restart.setTranslateY(300);
            restart.setOnAction(event -> main.restartGame());

            // Add the "Game Over" label and the restart button to the UI.
            main.root.getChildren().addAll(label, restart);

        });
    }

    // Display a "You Win" message.
    public void showWin(final Main main) {
        Platform.runLater(() -> {
            Label label = new Label("You Win :)");
            label.setTranslateX(200);
            label.setTranslateY(250);
            label.setScaleX(2);
            label.setScaleY(2);

            // Add the "You Win" label to the UI.
            main.root.getChildren().addAll(label);

        });
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
