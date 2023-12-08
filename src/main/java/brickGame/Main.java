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

/**
 * The Main class of the brick game application. This class extends the JavaFX Application class
 * and is responsible for initializing and launching the game's UI and managing the game's state.
 * It includes methods for scene transitions and updating game data.
 */

public class Main extends Application {

    // Constants for scene dimensions.
    public static final int SCENE_WIDTH = 500, SCENE_HEIGHT = 700;

    // Controller instances.
    private final UIController UIController = new UIController(this);
    private final GameLabelController gameLabelController = new GameLabelController(this);

    // UI components.
    private Scene scene;
    private Pane root;
    private FXMLLoader loader;

    // Game variables
    private GameController gameController;
    public boolean loadFromSave;
    private int finalScore = 0;


    /**
     * Initializes the UI elements and starts the primary stage of the application.
     *
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     * @throws IOException If an I/O error occurs during loading the FXML file for the UI.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        UIController.start(primaryStage);
    }

    /**
     * The main method that launches the JavaFX application.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Updates the game data, specifically the score and heart count.
     *
     * @param score The current score to update.
     * @param heart The current heart count to update.
     */
    public void updateGameData(int score, int heart) {
        gameLabelController.updateGameData(score, heart);
    }

    /**
     * Sets up the game UI, preparing it for gameplay.
     */
    public void setUpGameUI() {
        gameLabelController.setUpGameUI();
    }

    /**
     * Switches the current scene to the game scene.
     *
     * @throws IOException If an error occurs during loading the game scene FXML.
     */
    public void switchToGameScene() throws IOException {
        UIController.switchToGameScene();
    }

    /**
     * Displays the win screen UI.
     */
    public void showWin() {
        UIController.showWin();
    }

    /**
     * Displays the game over screen UI.
     */
    public void showGameOver() {
        UIController.showGameOver();
    }

    /**
     * Switches the current scene to the leaderboard scene.
     *
     * @throws IOException If an error occurs during loading the leaderboard scene FXML.
     */
    public void switchToLeaderboard() throws IOException {
        UIController.switchToLeaderboard();
    }

    /**
     * Switches the current scene to the menu scene.
     *
     * @throws IOException If an error occurs during loading the menu scene FXML.
     */
    public void switchToMenu() throws IOException {
        UIController.switchToMenu();
    }

    // Getter and setter methods.

    /**
     * Retrieves the GameController instance associated with this application.
     *
     * @return The GameController object managing game logic and state.
     */
    public GameController getGameController() {
        return gameController;
    }

    /**
     * Retrieves the FXMLLoader instance used for loading FXML resources.
     *
     * @return The FXMLLoader used for UI scene transitions.
     */
    public FXMLLoader getLoader() {
        return loader;
    }

    /**
     * Retrieves the current Scene of the application.
     *
     * @return The current Scene object displayed in the Stage.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Retrieves the root Pane of the current Scene.
     *
     * @return The root Pane of the current Scene.
     */
    public Pane getRoot() {
        return root;
    }

    /**
     * Retrieves the final score of the game.
     *
     * @return The final score achieved in the game.
     */
    public int getFinalScore() {
        return finalScore;
    }

    /**
     * Sets the GameController instance for this application.
     *
     * @param gameController The GameController to be set.
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Sets the FXMLLoader instance for this application.
     *
     * @param loader The FXMLLoader to be set for loading FXML resources.
     */
    public void setLoader(FXMLLoader loader) {
        this.loader = loader;
    }

    /**
     * Sets the Scene for the application.
     *
     * @param scene The Scene to be displayed in the Stage.
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * Sets the root Pane for the current Scene.
     *
     * @param root The root Pane to be set for the Scene.
     */
    public void setRoot(Pane root) {
        this.root = root;
    }

    /**
     * Sets the final score of the game.
     *
     * @param finalScore The final score to be set for the game.
     */
    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }
}
