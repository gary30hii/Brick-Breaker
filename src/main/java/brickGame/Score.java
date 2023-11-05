package brickGame;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


//import sun.plugin2.message.Message;

public class Score {

    // Display the score with animation at the specified (x, y) location.
    public void show(final double x, final double y, int score, final Main main) {
        String sign = (score >= 0) ? "+" : "";
        final Label label = new Label(sign + score);
        label.setTranslateX(x);
        label.setTranslateY(y);

        // Update the UI on the JavaFX application thread.
        Platform.runLater(() -> main.root.getChildren().add(label));

        // Animate the label's appearance.
        new Thread(() -> {
            for (int i = 0; i < 21; i++) {
                try {
                    label.setScaleX(i);
                    label.setScaleY(i);
                    label.setOpacity((20 - i) / 20.0);
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    System.err.println("Exception occurred: " + e.getMessage());
                }
            }
        }).start();
    }

    // Display a message with animation at a fixed position.
    public void showMessage(String message, final Main main) {
        final Label label = new Label(message);
        label.setTranslateX(220);
        label.setTranslateY(340);

        // Update the UI on the JavaFX application thread.
        Platform.runLater(() -> main.root.getChildren().add(label));

        // Animate the label's appearance.
        new Thread(() -> {
            for (int i = 0; i < 21; i++) {
                try {
                    label.setScaleX(Math.abs(i-10));
                    label.setScaleY(Math.abs(i-10));
                    label.setOpacity((20 - i) / 20.0);
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    System.err.println("Exception occurred: " + e.getMessage());
                }
            }
        }).start();
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
}
