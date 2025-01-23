package maze.maze.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Timer;

import maze.enums.Direction;
import maze.maze.MazeModel;
import maze.maze.element.StartModel;
import maze.maze.item.Item;
import maze.util.Observable;
import maze.window.AppScreenManager;
import maze.window.screen.MazeGameOverScreen;

public class PlayerModel extends maze.util.Observable {
    private float playerX = 1;
    private float playerY = 1;
    private boolean canMove = true;
    private int steps = 15;
    private final int DELAY = 1000 / 60;
    private Direction currentDirection;
    private List<Item> inventory;
    private boolean speedBoosteActive = false;
    private boolean speedBoosteRevert = false;

    MazeModel mazeModel = new MazeModel();

    // * PlayerのHP */
    private static final int MAX_HITPOINT = 3;
    private int hitPoint;

    // * PlayerのHPを回復 */
    public void heal(int amount) {
        hitPoint = Math.min(hitPoint + amount, MAX_HITPOINT);
        setChanged();
        notifyObservers("hpChanged");
    }

    // * Playerのスピードを一時的に上げる */
    public void boostSpeed() {
        if (!speedBoosteActive) {
            steps = 10;
            speedBoosteActive = true;
            setChanged();
            notifyObservers("speedBoosted");
        }

        Timer timer = new Timer(5000, e -> {
            if (canMove) {
                speedBoosteActive = false;
                steps = 15;
                setChanged();
                notifyObservers("speedBoosted");
                ((Timer) e.getSource()).stop();
            } else {
                speedBoosteRevert = true;
            }
            ((Timer) e.getSource()).stop();
        });
        timer.setRepeats(false);
        timer.start();
    }

    public PlayerModel(MazeModel mazeModel) {
        this.mazeModel = mazeModel;

        this.currentDirection = Direction.UP;

        this.inventory = new ArrayList<>();
        this.hitPoint = MAX_HITPOINT;

        this.mazeModel.addObserver((Observable observable, Object object) -> {
            setStartPos();
        });

        setStartPos();
    }

    public void addItem(Item item) {
        if (inventory.size() < 3) {
            inventory.add(item);
            setChanged();
            notifyObservers("inventoryChanged");
            System.out.println("Item added: " + item.getName());
        } else {
            System.out.println("Inventory is full");
        }
    }

    public void useItem(int index) {
        if (!mazeModel.isPaused() && canMove) {
            if (index >= 0 && index < inventory.size()) {
                Item item = inventory.get(index);
                item.applyEffect(this);
                inventory.remove(index);
                setChanged();
                notifyObservers("inventoryChanged");
                System.out.println("Item used: " + item.getName());
            } else {
                System.out.println("Invalid item index");
            }
        }

    }

    public List<Item> getInventory() {
        return Collections.unmodifiableList(this.inventory);
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
        System.out.println("hit");
        hitPoint--;
        setChanged();
        notifyObservers("hpChanged");

        if (hitPoint == 0) {
            // * ゲームオーバー処理 */
            System.out.println("Game Over");
            AppScreenManager.getInstance().push(new MazeGameOverScreen(mazeModel));
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
                        if (currentStep[0] < steps) {
                            playerX += deltaX / steps;
                            playerY += deltaY / steps;
                            notifyChange();
                            currentStep[0]++;
                        } else {
                            canMove = true;
                            ((Timer) e.getSource()).stop();
                            onMove();

                            if (speedBoosteRevert) {
                                steps = 15;
                                speedBoosteActive = false;
                                speedBoosteRevert = false;
                                setChanged();
                                notifyObservers("speedBoosted");
                            }
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
