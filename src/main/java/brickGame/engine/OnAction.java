package brickGame.engine;

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
