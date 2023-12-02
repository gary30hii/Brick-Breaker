package brickGame.controller;

import brickGame.Main;
import brickGame.model.Block;
import javafx.scene.control.Label;

public class GameLabelController {
    private final Main main;//UI variable
    public Label scoreLabel;
    public Label heartLabel;

    public GameLabelController(Main main) {
        this.main = main;
    }

    public void updateGameData(int score, int heart) {
        scoreLabel.setText("Score: " + score);
        heartLabel.setText("Heart : " + heart);
    }

    public void setUpGameUI() {
        scoreLabel = new Label("Score: " + main.getGameController().getScore());
        Label levelLabel = new Label("Level: " + main.getGameController().getLevel());
        heartLabel = new Label("Heart : " + main.getGameController().getHeart());

        // Add a style class to each label
        scoreLabel.getStyleClass().add("label-style");
        levelLabel.getStyleClass().add("label-style");
        heartLabel.getStyleClass().add("label-style");

        scoreLabel.setTranslateX(45);
        scoreLabel.setTranslateY(5);
        levelLabel.setTranslateX(45);
        levelLabel.setTranslateY(25);
        heartLabel.setTranslateX(380);
        heartLabel.setTranslateY(5);

        main.getRoot().getChildren().addAll(main.getGameController().getPaddle(), main.getGameController().getBall(), scoreLabel, heartLabel, levelLabel);

        for (Block block : main.getGameController().getBlocks()) {
            if (block.rect != null && !block.isDestroyed) {
                main.getRoot().getChildren().add(block.rect);
            }
        }
    }
}