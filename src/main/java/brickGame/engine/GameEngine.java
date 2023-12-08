package brickGame.engine;

import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The GameEngine class manages the game loop, including frame updates, physics calculations,
 * and time tracking. It uses an executor service to handle tasks concurrently.
 */
public class GameEngine {

    private static final Logger logger = LoggerFactory.getLogger(GameEngine.class);

    private OnAction onAction;
    private int fps = 15;
    private final ExecutorService executorService;
    public boolean isStopped = true;
    private long time = 0;

    /**
     * Constructs a GameEngine with a specified frame rate.
     *
     * @param fps The frame rate per second at which the game should run.
     */
    public GameEngine(int fps) {
        executorService = Executors.newFixedThreadPool(3);
        setFps(fps);
    }

    /**
     * Sets the action listener for game events.
     *
     * @param onAction The OnAction interface implementation to handle game actions.
     */
    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * Sets the frames per second for the game loop and converts it to milliseconds.
     *
     * @param fps The frames per second to set.
     */
    public void setFps(int fps) {
        this.fps = 600 / fps;
    }

    /**
     * Game frame update task.
     * Executes a loop in a separate thread to handle game frame updates at a regular interval defined by fps.
     * This method uses Platform.runLater to ensure updates are executed in the JavaFX Application Thread.
     */
    private void onUpdate() {
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Platform.runLater(() -> onAction.updateGameFrame());
                    Thread.sleep(fps);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    logger.error("An error occurred in Update() Method: " + e.getMessage(), e);
                    break;
                }
            }
        });
    }

    /**
     * Physics calculations task.
     * Executes a loop in a separate thread for performing physics calculations at a regular interval defined by fps.
     * This method schedules the physics calculation task to be executed in the JavaFX Application Thread.
     */
    private void onPhysicsCalculation() {
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Platform.runLater(() -> onAction.performPhysicsCalculations());
                    Thread.sleep(fps);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    logger.error("An error occurred in onPhysicsCalculation() Method: " + e.getMessage(), e);
                    break;
                }
            }
        });
    }

    /**
     * Time tracking task.
     * Executes a loop in a separate thread for tracking the passage of time in the game.
     * Increments a time counter every millisecond and calls the onTime method of the OnAction interface.
     */
    private void timeStart() {
        executorService.execute(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    time++;
                    onAction.onTime(time);
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                logger.error("An error occurred in timeStart() Method: " + e.getMessage(), e);

            }
        });
    }

    /**
     * Starts the game engine, beginning the game loop and enabling frame updates and physics calculations.
     */
    public void start() {
        time = 0;
        onUpdate();
        onPhysicsCalculation();
        timeStart();
        isStopped = false;
    }

    /**
     * Stops the game engine, halting all tasks and shutting down the executor service.
     */
    public void stop() {
        if (!isStopped) {
            isStopped = true;
            executorService.shutdownNow();
            try {
                if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * The OnAction interface defines methods for updating game frames, initializing the game,
     * performing physics calculations, and handling time updates.
     */
    public interface OnAction {
        void updateGameFrame();

        void onInit();

        void performPhysicsCalculations();

        void onTime(long time);
    }
}
