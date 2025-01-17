package maze.maze.player;

import javax.swing.Timer;

import maze.maze.MazeModel;
import maze.maze.element.StartModel;
import maze.util.Observable;

public class PlayerModel extends Observable {
    private float playerX = 1;
    private float playerY = 1;
    private boolean keyAcc = true;
    private boolean isWalkingUp = false;
    private boolean isWalkingDown = false;
    private boolean isWalkingLeft = false;
    private boolean isWalkingRight = false;
    private final int STEPS = 15;
    private final int DELAY = 1000 / 60;
    private Direction currentDirection;

    MazeModel mazeModel = new MazeModel();

    public PlayerModel(MazeModel mazeModel) {
        this.mazeModel = mazeModel;

        this.currentDirection = Direction.FORWARD;

        this.mazeModel.addObserver((Observable observable, Object object) -> {
            setStartPos();
        });

        setStartPos();
    }

    private void setStartPos() {
        int startPos[] = mazeModel.locateElement(StartModel.class);
        if (startPos != null) {
            playerX = startPos[0];
            playerY = startPos[1];
        }
    }

    public float getPlayerX() {
        return playerX;
    }

    public float getPlayerY() {
        return playerY;
    }

    public boolean isIdle() {
        return keyAcc;
    }

    public boolean isWalkingUp() {
        return isWalkingUp;
    }

    public boolean isWalkingDown() {
        return isWalkingDown;
    }

    public boolean isWalkingLeft() {
        return isWalkingLeft;
    }

    public boolean isWalkingRight() {
        return isWalkingRight;
    }

    public enum Direction {
        FORWARD, BACK, LEFT, RIGHT
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    // * 連続的な動き試作 */
    public void moveLeft() {
        if (mazeModel.getElementAt(Math.round(playerX - 1), Math.round(playerY)).canEnter()) {
            if (keyAcc) {
                isWalkingLeft = true;
                currentDirection = Direction.LEFT;
                final int[] currentStep = { 0 };
                keyAcc = false;
                Timer timer = new Timer(DELAY, e -> {
                    if (mazeModel.isPaused())
                        return;
                    if (currentStep[0] < STEPS) {
                        playerX -= 1.0 / STEPS;
                        notifyChange();
                        currentStep[0]++;
                    } else {
                        keyAcc = true;
                        isWalkingLeft = false;
                        ((Timer) e.getSource()).stop();
                        onMove();
                    }
                });
                timer.start();
            }
        }
    }

    public void moveRight() {
        if (mazeModel.getElementAt(Math.round(playerX + 1), Math.round(playerY)).canEnter()) {
            if (keyAcc) {
                isWalkingRight = true;
                currentDirection = Direction.RIGHT;
                final int[] currentStep = { 0 };
                keyAcc = false;
                Timer timer = new Timer(DELAY, e -> {
                    if (mazeModel.isPaused())
                        return;
                    if (currentStep[0] < STEPS) {
                        playerX += 1.0 / STEPS;
                        notifyChange();
                        currentStep[0]++;
                    } else {
                        keyAcc = true;
                        isWalkingRight = false;
                        ((Timer) e.getSource()).stop();
                        onMove();
                    }
                });
                timer.start();
            }
        }
    }

    public void moveUp() {
        if (mazeModel.getElementAt(Math.round(playerX), Math.round(playerY - 1)).canEnter()) {
            if (keyAcc) {
                isWalkingUp = true;
                currentDirection = Direction.FORWARD;
                final int[] currentStep = { 0 };
                keyAcc = false;
                Timer timer = new Timer(DELAY, e -> {
                    if (mazeModel.isPaused())
                        return;
                    if (currentStep[0] < STEPS) {
                        playerY -= 1.0 / STEPS;
                        notifyChange();
                        currentStep[0]++;
                    } else {
                        keyAcc = true;
                        isWalkingUp = false;
                        ((Timer) e.getSource()).stop();
                        isWalkingUp = false;
                        onMove();
                    }
                });
                timer.start();
            }
        }
    }

    public void moveDown() {
        if (mazeModel.getElementAt(Math.round(playerX), Math.round(playerY + 1)).canEnter()) {
            if (keyAcc) {
                isWalkingDown = true;
                currentDirection = Direction.BACK;
                final int[] currentStep = { 0 };
                keyAcc = false;
                Timer timer = new Timer(DELAY, e -> {
                    if (mazeModel.isPaused())
                        return;
                    if (currentStep[0] < STEPS) {
                        playerY += 1.0 / STEPS;
                        notifyChange();
                        currentStep[0]++;
                    } else {
                        keyAcc = true;
                        isWalkingDown = false;
                        ((Timer) e.getSource()).stop();
                        onMove();
                    }
                });
                timer.start();
            }
        }
    }

    private void onMove() {
        mazeModel.getElementAt(Math.round(playerX), Math.round(playerY)).onEnter();
    }

    private void notifyChange() {
        setChanged();
        notifyObservers();
    }
}
