package maze.maze.player;

import javax.swing.Timer;

import maze.enums.Direction;
import maze.maze.MazeModel;
import maze.maze.element.StartModel;
import maze.util.Observable;
import maze.window.AppScreenManager;
import maze.window.screen.MazeGameOverScreen;

public class PlayerModel extends Observable {
    private float playerX = 1;
    private float playerY = 1;
    private boolean canMove = true;
    private final int STEPS = 15;
    private final int DELAY = 1000 / 60;
    private Direction currentDirection;

    MazeModel mazeModel = new MazeModel();

    // * PlayerのHP */
    private int hitPoint = 3;

    public PlayerModel(MazeModel mazeModel) {
        this.mazeModel = mazeModel;

        this.currentDirection = Direction.UP;

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

    public int getHitPoint() {
        return hitPoint;
    }

    public boolean isIdle() {
        return canMove;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public void onHit() {
        hitPoint--;
        if (hitPoint == 0) {
            // * ゲームオーバー処理 */
            System.out.println("Game Over");
            AppScreenManager.getInstance().push(new MazeGameOverScreen());
        }
    }

    public void moveUp() {
        move(0, -1, Direction.UP);
    }

    public void moveDown() {
        move(0, 1, Direction.DOWN);
    }

    public void moveLeft() {
        move(-1, 0, Direction.LEFT);
    }

    public void moveRight() {
        move(1, 0, Direction.RIGHT);
    }

    private void move(float deltaX, float deltaY, Direction direction) {
        int targetX = Math.round(playerX + deltaX);
        int targetY = Math.round(playerY + deltaY);
        if (mazeModel.isInMaze(targetX, targetY)) {
            if (mazeModel.getElementAt(targetX, targetY).canEnter()) {
                if (canMove) {
                    currentDirection = direction;
                    final int[] currentStep = { 0 };
                    canMove = false;
                    Timer timer = new Timer(DELAY, e -> {
                        if (mazeModel.isPaused())
                            return;
                        if (currentStep[0] < STEPS) {
                            playerX += deltaX / STEPS;
                            playerY += deltaY / STEPS;
                            notifyChange();
                            currentStep[0]++;
                        } else {
                            canMove = true;
                            ((Timer) e.getSource()).stop();
                            onMove();
                        }
                    });
                    timer.start();
                }
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
