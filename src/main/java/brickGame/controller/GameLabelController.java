package brickGame.controller;

import brickGame.Main;
import brickGame.model.Block;
import javafx.scene.control.Label;

/**
 * Controller class for managing game labels.
 * This class is responsible for updating and setting up the UI labels that display the game's score and hearts.
 */
public class GameLabelController {
    private final Main main;//UI variable
    private Label scoreLabel;
    private Label heartLabel;

     /**
     * Constructor for GameLabelController.
     * Initializes the controller with a reference to the main game UI class.
     *
     * @param main The main UI class which is used to interact with the game's UI elements.
     */
    public GameLabelController(Main main) {
        this.main = main;
    }

    /**
     * Updates the score and heart labels with the current game data.
     *
     * @param score The current score to display.
     * @param heart The current number of hearts to display.
     */
    public void updateGameData(int score, int heart) {
        getScoreLabel().setText("Score: " + score);
        getHeartLabel().setText("Heart : " + heart);
    }

    /**
     * Sets up the game UI labels for score, level, and hearts.
     * Initializes the labels, applies a CSS style class, and positions them within the game UI.
     * Also adds the game paddle, ball, labels, and blocks to the root pane.
     */
    public void setUpGameUI() {
        setScoreLabel(new Label("Score: " + getMain().getGameController().getScore()));
        Label levelLabel = new Label("Level: " + getMain().getGameController().getLevel());
        setHeartLabel(new Label("Heart : " + getMain().getGameController().getHeart()));

        // Add a style class to each label
        getScoreLabel().getStyleClass().add("label-style");
        levelLabel.getStyleClass().add("label-style");
        getHeartLabel().getStyleClass().add("label-style");

        // Set position of the labels in the game UI.
        getScoreLabel().setTranslateX(45);
        getScoreLabel().setTranslateY(5);
        levelLabel.setTranslateX(45);
        levelLabel.setTranslateY(25);
        getHeartLabel().setTranslateX(380);
        getHeartLabel().setTranslateY(5);

        getMain().getRoot().getChildren().addAll(getMain().getGameController().getPaddle(), getMain().getGameController().getBall(), getScoreLabel(), getHeartLabel(), levelLabel);

        // Add blocks to the UI, only if they are not destroyed.
        for (Block block : getMain().getGameController().getBlocks()) {
            if (block.rect != null && !block.isDestroyed) {
                getMain().getRoot().getChildren().add(block.rect);
            }
        }
    }

    // Getters and Setters

    /**
     * Gets the main game UI class.
     *
     * @return The main game UI class.
     */
    public Main getMain() {
        return main;
    }

    /**
     * Gets the label used for displaying the score.
     *
     * @return The score label.
     */
    public Label getScoreLabel() {
        return scoreLabel;
    }

    /**
     * Sets the label used for displaying the score.
     *
     * @param scoreLabel The score label to set.
     */
    public void setScoreLabel(Label scoreLabel) {
        this.scoreLabel = scoreLabel;
    }

    /**
     * Gets the label used for displaying the hearts.
     *
     * @return The heart label.
     */
    public Label getHeartLabel() {
        return heartLabel;
    }

    /**
     * Sets the label used for displaying the hearts.
     *
     * @param heartLabel The heart label to set.
     */
    public void setHeartLabel(Label heartLabel) {
        this.heartLabel = heartLabel;
    }
}