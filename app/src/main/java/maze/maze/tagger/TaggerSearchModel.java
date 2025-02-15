package maze.maze.tagger;

import java.util.ArrayDeque;
import maze.maze.MazeModel;
import maze.maze.player.PlayerModel;

// 座標クラス
class Coordinate {
  int x;
  int y;

  public Coordinate(int x, int y) {
    this.x = x;
    this.y = y;
  }
}

public class TaggerSearchModel {
  private final MazeModel mazeModel;
  private final PlayerModel playerModel;
  private final TaggerModel taggerModel;

  private int mazeWidth;
  private int mazeHeight;
  private int[][] dist;
  private Coordinate start;
  private Coordinate goal;
  private final int[] dx = { 1, 0, -1, 0 };
  private final int[] dy = { 0, 1, 0, -1 };
  private final Object movementMonitor = new Object();
  private static final int STEP_LIMIT = 1;
  private static final int STOP_DURATION = 3000;
  private static final int TAGGER_RANGE = 12;

  public TaggerSearchModel(MazeModel mazeModel, PlayerModel playerModel, TaggerModel taggerModel) {
    this.mazeModel = mazeModel;
    this.playerModel = playerModel;
    this.taggerModel = taggerModel;

    // プレイヤーのスタート位置を初期ゴール地点に設定
    goal = new Coordinate(Math.round(playerModel.getPlayerX()), Math.round(playerModel.getPlayerY()));

    // dist[][] を初期化
    initializeDistance();
  }

  /**
   * 距離の初期化
   */
  private void initializeDistance() {
    mazeWidth = mazeModel.getMazeWidth();
    mazeHeight = mazeModel.getMazeHeight();
    dist = new int[mazeWidth][mazeHeight];
    for (int x = 0; x < mazeWidth; x++) {
      for (int y = 0; y < mazeHeight; y++) {
        dist[x][y] = -1;
      }
    }
  }

  /**
   * ランダムウォーク
   */
  public void randomWalk() {
    int random = (int) (Math.random() * 4);
    switch (random) {
      case 0 -> taggerModel.moveLeft();
      case 1 -> taggerModel.moveRight();
      case 2 -> taggerModel.moveUp();
      case 3 -> taggerModel.moveDown();
    }
  }

  /**
   * 幅優先探索
   */
  private ArrayDeque<Coordinate> performBFS() {
    initializeDistance();
    start = new Coordinate(Math.round(taggerModel.getTaggerX()), Math.round(taggerModel.getTaggerY()));
    ArrayDeque<Coordinate> queue = new ArrayDeque<>();
    queue.add(start);
    dist[start.x][start.y] = 0;

    // * プレイヤーの現在地までの距離を計測 */
    while (!queue.isEmpty()) {
      Coordinate elem = queue.poll();

      for (int i = 0; i < 4; i++) {
        int nx = elem.x + dx[i];
        int ny = elem.y + dy[i];

        if (nx >= 0 && nx < mazeWidth && ny >= 0 && ny < mazeHeight) {
          if (dist[nx][ny] == -1 && mazeModel.getElementAt(nx, ny).canEnter()) {
            Coordinate nextElem = new Coordinate(nx, ny);
            queue.add(nextElem);
            dist[nx][ny] = dist[elem.x][elem.y] + 1;
          }
        }
      }
      // * プレイヤー位置までの距離が求まったらそこで探索終了 */
      if (dist[goal.x][goal.y] != -1) {
        break;
      }
    }

    ArrayDeque<Coordinate> stack = new ArrayDeque<>();
    stack.add(goal);
    Coordinate elem = goal;
    int ptDistance = dist[goal.x][goal.y];

    while (ptDistance > 1) {
      for (int i = 0; i < 4; i++) {
        int nx = elem.x + dx[i];
        int ny = elem.y + dy[i];

        if (nx >= 0 && nx < mazeWidth && ny >= 0 && ny < mazeHeight && dist[nx][ny] == ptDistance - 1) {
          Coordinate nextElem = new Coordinate(nx, ny);
          stack.add(nextElem);
          elem = nextElem;
          ptDistance--;
          break;
        }
      }
    }
    return stack;
  }

  // * taggerの移動処理 */
  private void moveTowardPlayer(ArrayDeque<Coordinate> path, int stepLimit) {
    int stepsTaken = 0;

    while (!path.isEmpty() && stepsTaken < stepLimit) {
      Coordinate next = path.pollLast();
      // * taggerの前の移動が終わる(整数座標になる)まで待つ */
      if (next != null) {
        try {
          waitForMoveFinished();
        } catch (InterruptedException ex) {
          Thread.currentThread().interrupt();
          return;
        }

        int currentX = Math.round(taggerModel.getTaggerX());
        int currentY = Math.round(taggerModel.getTaggerY());

        if (next.x == currentX + 1) {
          taggerModel.moveRight();
        } else if (next.x == currentX - 1) {
          taggerModel.moveLeft();
        } else if (next.y == currentY + 1) {
          taggerModel.moveDown();
        } else if (next.y == currentY - 1) {
          taggerModel.moveUp();
        }
      }
      stepsTaken++;

      // * taggerがプレイヤーに到達したら一時停止 */
      if (taggerModel.isTaggerArrived()) {
        playerModel.onHit();
        try {
          Thread.sleep(STOP_DURATION);
          taggerModel.setTaggerArrivedFlag(false);
        } catch (InterruptedException ex) {
          Thread.currentThread().interrupt();
          return;
        }
      }
    }
  }

  // taggerの移動が終わるまで待つ
  private void waitForMoveFinished() throws InterruptedException {
    synchronized (movementMonitor) {
      while (!taggerModel.getCanMoveFlag()) {
        movementMonitor.wait();
      }
    }
  }

  // 条件が達成されたのを知らせる
  public void signalConditionMet() {
    synchronized (movementMonitor) {
      movementMonitor.notifyAll();
    }
  }

  // 鬼の探索とランダムウォークの実施機構
  public void executeTaggerMovement() {
    while (true) {
      goal.x = Math.round(playerModel.getPlayerX());
      goal.y = Math.round(playerModel.getPlayerY());
      // プレイヤーの位置によって探索かランダムウォークを選択
      if (isTaggerinRange()) {
        ArrayDeque<Coordinate> path = performBFS();
        moveTowardPlayer(path, STEP_LIMIT);
      } else {
        randomWalk();
        try {
          Thread.sleep(100);
          waitForMoveFinished();
        } catch (InterruptedException ex) {
          Thread.currentThread().interrupt();
          return;
        }
      }
    }
  }

  // プレイヤーと鬼が同じ座標にいるかどうかの判定
  public boolean isTaggerAtPlayer() {
    int taggerX = Math.round(taggerModel.getTaggerX());
    int taggerY = Math.round(taggerModel.getTaggerY());
    int playerX = Math.round(playerModel.getPlayerX());
    int playerY = Math.round(playerModel.getPlayerY());
    return taggerX == playerX && taggerY == playerY;
  }

  // プレイヤーと鬼が一定の距離以内にいるかどうかの判定
  public boolean isTaggerinRange() {
    int taggerX = Math.round(taggerModel.getTaggerX());
    int taggerY = Math.round(taggerModel.getTaggerY());
    int playerX = Math.round(playerModel.getPlayerX());
    int playerY = Math.round(playerModel.getPlayerY());
    int dx = taggerX - playerX;
    int dy = taggerY - playerY;
    return dx * dx + dy * dy <= TAGGER_RANGE * TAGGER_RANGE;
  }
}
