package brickGame;


public class GameEngine {

    private OnAction onAction;
    private int fps = 15;
    private Thread updateThread;
    private Thread physicsThread;
    private Thread timeThread;
    private long time = 0;
    public boolean isStopped = true;

    //A setter method to set the callback object that implements the OnAction interface. This object will receive notifications when specific actions occur in the game engine.
    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * @param fps set fps and we convert it to millisecond
     */
    public void setFps(int fps) {
        this.fps = 1000 / fps;
    }

    //A method that manages the game's update loop. It repeatedly calls the onUpdate method provided by the callback object. This method is executed on a separate thread.
    private synchronized void Update() {
        updateThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    onAction.onUpdate();
                    Thread.sleep(fps);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore the interrupted status
                    System.err.println("Exception occurred: " + e.getMessage());
                    break; // Exit the loop gracefully
                }
            }
        });
        updateThread.start();
    }

    //initializes the game by calling the onInit method provided by the callback object
    private void Initialize() {
        onAction.onInit();
    }

    //manages the physics calculation loop. It repeatedly calls the onPhysicsUpdate method provided by the callback object. This method is executed on a separate thread.
    private synchronized void PhysicsCalculation() {
        physicsThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    onAction.onPhysicsUpdate();
                    Thread.sleep(fps);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore the interrupted status
                    System.err.println("Exception occurred: " + e.getMessage());
                    break; // Exit the loop gracefully
                }
            }
        });
        physicsThread.start();
    }

    //start the time-tracking thread, which increments the time variable and notifies the callback object of the elapsed time.
    private void TimeStart() {
        timeThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    time++;
                    onAction.onTime(time);
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore the interrupted status
                    System.err.println("Exception occurred: " + e.getMessage());
                    break; // Exit the loop gracefully
                }
            }
        });
        timeThread.start();
    }

    //start the game engine. It calls the Initialize, Update, and PhysicsCalculation methods, as well as starts a time-tracking thread.
    public void start() {
        time = 0;
        Initialize();
        Update();
        PhysicsCalculation();
        TimeStart();
        isStopped = false;
    }

    //stop the game engine. It interrupts the update thread, physics thread, and time thread to halt the game's execution.
    public void stop() {
        if (!isStopped) {
            isStopped = true;
            updateThread.interrupt();
            physicsThread.interrupt();
            timeThread.interrupt();
        }
    }

    public interface OnAction {
        void onUpdate();

        void onInit();

        void onPhysicsUpdate();

        void onTime(long time);
    }

}
