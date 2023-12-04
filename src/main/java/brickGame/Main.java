package brickGame;

import brickGame.controller.GameController;
import brickGame.controller.UIController;
import brickGame.controller.GameLabelController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    // Constants
    public static final int SCENE_WIDTH = 500, SCENE_HEIGHT = 700;
    private final UIController UIController = new UIController(this);
    private final GameLabelController gameLabelController = new GameLabelController(this);
    private Scene scene;
    private Pane root;
    private FXMLLoader loader;

    // Game variables
    private GameController gameController;
    public boolean loadFromSave;
    public int finalScore = 0;


    // Initialize UI elements
    @Override
    public void start(Stage primaryStage) throws IOException {
        UIController.start(primaryStage);
    }

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args);
    }

    public void updateGameData(int score, int heart){
        gameLabelController.updateGameData(score, heart);
    }

    public void setUpGameUI() {
        gameLabelController.setUpGameUI();
    }

    public void switchToGameScene() throws IOException {
        UIController.switchToGameScene();
    }

    public void showWin() {
        UIController.showWin();
    }

    public void showGameOver() {
        UIController.showGameOver();
    }

    public void switchToLeaderboard() throws IOException {
        UIController.switchToLeaderboard();
    }


    public void switchToMenu() throws IOException {
        UIController.switchToMenu();
    }

    public GameController getGameController() {
        return gameController;
    }

    public FXMLLoader getLoader() {
        return loader;
    }

    public Scene getScene() {
        return scene;
    }

    public Pane getRoot() {
        return root;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setLoader(FXMLLoader loader) {
        this.loader = loader;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setRoot(Pane root) {
        this.root = root;
    }
}
