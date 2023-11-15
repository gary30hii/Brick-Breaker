package brickGame.engine;

import javafx.animation.AnimationTimer;

public class GameEngine {

    private OnAction onAction;
    private boolean isStopped = true;
    private int frameDelay = 66; // Default for ~15 fps (1000ms / 15)
    private long lastUpdateTime = 0;

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
        this.frameDelay = 120 / fps;
    }

    private void gameLoop() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isStopped) {
                    if (lastUpdateTime == 0) {
                        lastUpdateTime = now;
                    }

                    long elapsedNanoSeconds = now - lastUpdateTime;
                    long elapsedMilliseconds = elapsedNanoSeconds / 1_000_000;

                    if (elapsedMilliseconds > frameDelay) {
                        onAction.onUpdate();
                        onAction.onPhysicsUpdate();
                        onAction.onTime(System.currentTimeMillis());
                        lastUpdateTime = now;
                    }
                }
            }
        }.start();
    }

    public void start() {
        if (!isStopped) {
            return; // The game is already running
        }
        isStopped = false;
        onAction.onInit();
        gameLoop();
    }

    public void stop() {
        isStopped = true;
        lastUpdateTime = 0;
    }
}
