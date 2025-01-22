package maze.maze.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.Timer;

import maze.enums.Direction;
import maze.maze.MazeModel;
import maze.maze.element.StartModel;
import maze.util.Observable;
import maze.window.AppScreenManager;
import maze.window.screen.MazeGameOverScreen;
import maze.maze.item.Item;
import maze.maze.item.ItemModel;

public class PlayerModel extends maze.util.Observable {
    private float playerX = 1;
    private float playerY = 1;
    private boolean keyAcc = true;
    private boolean isWalkingUp = false;
    private boolean isWalkingDown = false;
    private boolean isWalkingLeft = false;
    private boolean isWalkingRight = false;
    private int steps = 15;
    private final int DELAY = 1000 / 60;
    private Direction currentDirection;
    private List<Item> inventory;

    MazeModel mazeModel = new MazeModel();

    // * PlayerのHP */
    private static final int MAX_HITPOINT = 3;
    private int hitPoint;

    public void heal(int amount) {
        hitPoint = Math.min(hitPoint + amount, MAX_HITPOINT);
        setChanged();
        notifyObservers();
    }

    public void boostSpeed() {
        steps = 7;
        setChanged();
        notifyObservers();

        Timer timer = new Timer(5000, e -> {
            steps = 15;
            setChanged();
            notifyObservers();
            ((Timer) e.getSource()).stop();
        });
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
        if (!mazeModel.isPaused() && keyAcc) {
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
                    if (currentStep[0] < steps) {
                        playerX -= 1.0 / steps;
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
                    if (currentStep[0] < steps) {
                        playerX += 1.0 / steps;
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
                currentDirection = Direction.UP;
                final int[] currentStep = { 0 };
                keyAcc = false;
                Timer timer = new Timer(DELAY, e -> {
                    if (mazeModel.isPaused())
                        return;
                    if (currentStep[0] < steps) {
                        playerY -= 1.0 / steps;
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
                currentDirection = Direction.DOWN;
                final int[] currentStep = { 0 };
                keyAcc = false;
                Timer timer = new Timer(DELAY, e -> {
                    if (mazeModel.isPaused())
                        return;
                    if (currentStep[0] < steps) {
                        playerY += 1.0 / steps;
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
