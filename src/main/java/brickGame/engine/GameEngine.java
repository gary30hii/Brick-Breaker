package brickGame.engine;

import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameEngine {

    private static final Logger logger = LoggerFactory.getLogger(GameEngine.class);

    private OnAction onAction;
    private int fps = 15;
    private final ExecutorService executorService;
    public boolean isStopped = true;
    private long time = 0;

    public GameEngine(int fps) {
        executorService = Executors.newFixedThreadPool(3);
        setFps(fps);
    }

    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * @param fps set fps and we convert it to millisecond
     */
    public void setFps(int fps) {
        this.fps = 360 / fps;
    }

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

    public void start() {
        time = 0;
        onUpdate();
        onPhysicsCalculation();
        timeStart();
        isStopped = false;
    }

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

    public interface OnAction {
        void updateGameFrame();

        void onInit();

        void performPhysicsCalculations();

        void onTime(long time);
    }
}
