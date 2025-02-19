package maze.maze.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Timer;

import maze.enums.Direction;
import maze.maze.MazeModel;
import maze.maze.element.PlayerStartModel;
import maze.maze.item.Item;
import maze.util.Observable;
import maze.window.AppScreenManager;
import maze.window.screen.MazeGameOverScreen;

public class PlayerModel extends maze.util.Observable {
    // 最大インベントリサイズ
    private static final int MAX_INVENTORY_SIZE = 3;
    // 通常時のステップ数
    private static final int NORMAL_STEPS = 15;
    // スピードブースト時のステップ数
    private static final int BOOSTED_STEPS = 10;
    // フレーム遅延
    private static final int FRAME_DELAY = 1000 / 60;
    // 最大HP
    private static final int MAX_HITPOINT = 3;
    // スピードブースト効果時間
    private static final int BOOST_DURATION = 5000;

    // イベント名定義
    public static final String HP_CHANGED_EVENT = "hpChanged";
    public static final String SPEED_BOOSTED_EVENT = "speedBoosted";
    public static final String INVENTORY_CHANGED_EVENT = "inventoryChanged";

    // プレイヤーの座標
    private float playerX;
    private float playerY;

    // 入力受付状態
    private boolean canMove = true;

    // プレイヤーが向いている方向
    private Direction currentDirection;

    // 移動時のステップ数
    private int steps = NORMAL_STEPS;

    // アイテムインベントリ
    private List<Item> inventory;

    // スピードブースト関連フラグ
    private boolean speedBoostActive = false;
    private boolean speedBoostRevert = false;

    public MazeModel mazeModel;

    // PlayerのHP
    private int hitPoint;

    // コンストラクタ
    public PlayerModel(MazeModel mazeModel) {
        this.mazeModel = mazeModel;
        this.currentDirection = Direction.UP;
        this.inventory = new ArrayList<>();
        this.hitPoint = MAX_HITPOINT;

        this.mazeModel.addObserver((Observable observable, Object object) -> {
            setStartPos();
        });
    }

    // HP回復処理
    public void heal(int amount) {
        hitPoint = Math.min(hitPoint + amount, MAX_HITPOINT);
        notifyChange(HP_CHANGED_EVENT);
    }

    // スピードブースト処理
    public void boostSpeed() {
        if (!speedBoostActive) {
            steps = BOOSTED_STEPS;
            speedBoostActive = true;
            notifyChange(SPEED_BOOSTED_EVENT);
        }

        Timer timer = new Timer(BOOST_DURATION, e -> {
            // プレイヤーが移動可能な状態であればスピードブースト解除
            if (canMove) {
                revertSpeedBoost();
            } else {
                // プレイヤーが移動中であれば、移動完了後にスピードブースト解除のためのフラグを立てる
                speedBoostRevert = true;
            }
            ((Timer) e.getSource()).stop();
        });
        timer.setRepeats(false);
        timer.start();
    }

    // スピードブースト解除
    private void revertSpeedBoost() {
        steps = NORMAL_STEPS;
        speedBoostActive = false;
        notifyChange(SPEED_BOOSTED_EVENT);
    }

    // アイテム追加
    public void addItem(Item item) {
        if (inventory.size() < MAX_INVENTORY_SIZE) {
            inventory.add(item);
            notifyChange(INVENTORY_CHANGED_EVENT);
        }
    }

    // アイテム使用
    public void useItem(int index) {
        if (!mazeModel.isPaused() && canMove) {
            if (index >= 0 && index < inventory.size()) {
                Item item = inventory.get(index);
                item.applyEffect(this);
                inventory.remove(index);
                notifyChange(INVENTORY_CHANGED_EVENT);
            }
        }
    }

    // インベントリ情報取得
    public List<Item> getInventory() {
        return Collections.unmodifiableList(this.inventory);
    }

    // インベントリが満杯かどうか
    public boolean isInventoryFull() {
        return inventory.size() >= MAX_INVENTORY_SIZE;
    }

    // スタート位置設定
    private void setStartPos() {
        int startPos[] = mazeModel.locateElement(PlayerStartModel.class);
        if (startPos != null) {
            playerX = startPos[0];
            playerY = startPos[1];
        }
    }

    // プレイヤーのX座標を返す
    public float getPlayerX() {
        return playerX;
    }

    // プレイヤーのY座標を返す
    public float getPlayerY() {
        return playerY;
    }

    // プレイヤーのHPを返す
    public int getHitPoint() {
        return hitPoint;
    }

    // プレイヤーがアイドル状態かどうか
    public boolean isIdle() {
        return canMove;
    }

    // プレイヤーが向いている方向を返す
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    // ポーズ状態かどうか
    public boolean isPaused() {
        return mazeModel.isPaused();
    }

    // 迷路の幅を返す
    public int getMazeWidth() {
        return mazeModel.getMazeWidth();
    }

    // 迷路の高さを返す
    public int getMazeHeight() {
        return mazeModel.getMazeHeight();
    }

    // 鬼との衝突時の処理
    public void onHit() {
        hitPoint--;
        notifyChange(HP_CHANGED_EVENT);
        // HPが0になったらゲームオーバー
        if (hitPoint == 0) {
            AppScreenManager.getInstance().push(new MazeGameOverScreen(mazeModel));
        }
    }

    // 上に移動
    public void moveUp() {
        move(0, -1, Direction.UP);
    }

    // 下に移動
    public void moveDown() {
        move(0, 1, Direction.DOWN);
    }

    // 左に移動
    public void moveLeft() {
        move(-1, 0, Direction.LEFT);
    }

    // 右に移動
    public void moveRight() {
        move(1, 0, Direction.RIGHT);
    }

    // プレイヤーの移動処理
    private void move(float deltaX, float deltaY, Direction direction) {
        int targetX = Math.round(playerX + deltaX);
        int targetY = Math.round(playerY + deltaY);
        if (mazeModel.isInMaze(targetX, targetY) && mazeModel.getElementAt(targetX, targetY).canEnter()) {
            if (canMove) {
                currentDirection = direction;
                canMove = false;
                startMoveAnimation(deltaX, deltaY);
            }
        }
    }

    // プレイヤーの移動アニメーション処理
    private void startMoveAnimation(float deltaX, float deltaY) {
        final int[] currentStep = { 0 };
        Timer moveTimer = new Timer(FRAME_DELAY, null);
        moveTimer.addActionListener(e -> {
            if(mazeModel.isPaused()) {
                return;
            }
            if (currentStep[0] < steps) {
                playerX += deltaX / steps;
                playerY += deltaY / steps;
                currentStep[0]++;
                notifyChange();
            } else {
                canMove = true;
                moveTimer.stop();
                onMove();
                // スピードブースト解除フラグが立っている場合、スピードブーストを解除
                if (speedBoostRevert) {
                    revertSpeedBoost();
                }
            }
        });
        moveTimer.start();
    }

    // 移動後の処理
    private void onMove() {
        mazeModel.getElementAt(Math.round(playerX), Math.round(playerY)).onEnter();
    }

    // Observerパターンの通知処理
    private void notifyChange() {
        setChanged();
        notifyObservers();
    }

    private void notifyChange(String event) {
        setChanged();
        notifyObservers(event);
    }
}
