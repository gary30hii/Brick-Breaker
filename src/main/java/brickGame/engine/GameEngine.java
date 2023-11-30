package brickGame.engine;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class GameEngine {

    private OnAction onAction;
    private int fps = 15;
    private ExecutorService executorService;
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

    private void Update() {
        Runnable updateTask = () -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    onAction.updateGameFrame();
                    Thread.sleep(fps);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        executorService.submit(updateTask);
    }

    private void PhysicsCalculation() {
        Runnable physicsTask = () -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    onAction.performPhysicsCalculations();
                    Thread.sleep(fps);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        executorService.submit(physicsTask);
    }

    private void TimeStart() {
        Runnable timeTask = () -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    time++;
                    onAction.onTime(time);
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        executorService.submit(timeTask);
    }

    public void start() {
        time = 0;
        isStopped = false;
        Update();
        PhysicsCalculation();
        TimeStart();
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
