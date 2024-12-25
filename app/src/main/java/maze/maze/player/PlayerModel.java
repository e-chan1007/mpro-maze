package maze.maze.player;

import javax.swing.Timer;

import maze.maze.MazeModel;
import maze.maze.element.StartModel;
import maze.util.Observable;

public class PlayerModel extends Observable {
    private float playerX = 1;
    private float playerY = 1;
    private boolean keyAcc = true;
    private final int STEPS = 15;
    private final int DELAY = 1;
    MazeModel mazeModel = new MazeModel();

    public PlayerModel(MazeModel mazeModel) {
        this.mazeModel = mazeModel;

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

    // * 連続的な動き試作 */
    public void moveLeft() {
        if (mazeModel.getElementAt(Math.round(playerX - 1), Math.round(playerY)).canEnter()) {
            if (keyAcc) {
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
                        ((Timer) e.getSource()).stop();
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
