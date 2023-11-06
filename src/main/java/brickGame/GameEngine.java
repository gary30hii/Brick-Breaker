package brickGame;

public class GameEngine {

    private OnAction onAction;
    private volatile boolean isStopped = true;
    private int frameDelay = 66; // Default for ~15 fps (1000ms / 15)
    private Thread updateThread;
    private Thread physicsThread;
    private Thread timeThread;
    private long time = 0;

    public interface OnAction {
        void onUpdate();
        void onInit();
        void onPhysicsUpdate();
        void onTime(long time);
    }

    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    public void setFps(int fps) {
        if (fps <= 0) {
            throw new IllegalArgumentException("FPS must be greater than zero");
        }
        this.frameDelay = 1000 / fps;
    }

    private void update() {
        updateThread = new Thread(() -> {
            while (!isStopped) {
                try {
                    onAction.onUpdate();
                    Thread.sleep(frameDelay);
                } catch (InterruptedException e) {
                    if (isStopped) {
                        break;
                    }
                    Thread.currentThread().interrupt();
                }
            }
        });
        updateThread.start();
    }

    private void initialize() {
        onAction.onInit();
    }

    private void physicsCalculation() {
        physicsThread = new Thread(() -> {
            while (!isStopped) {
                try {
                    onAction.onPhysicsUpdate();
                    Thread.sleep(frameDelay);
                } catch (InterruptedException e) {
                    if (isStopped) {
                        break;
                    }
                    Thread.currentThread().interrupt();
                }
            }
        });
        physicsThread.start();
    }

    private void timeStart() {
        timeThread = new Thread(() -> {
            while (!isStopped) {
                try {
                    time++;
                    onAction.onTime(time);
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    if (isStopped) {
                        break;
                    }
                    Thread.currentThread().interrupt();
                }
            }
        });
        timeThread.start();
    }

    public void start() {
        if (!isStopped) {
            return; // The game is already running
        }
        isStopped = false;
        time = 0;
        initialize();
        update();
        physicsCalculation();
        timeStart();
    }

    public void stop() {
        isStopped = true;

        updateThread.interrupt();
        physicsThread.interrupt();
        timeThread.interrupt();

        try {
            updateThread.join();
            physicsThread.join();
            timeThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}