package maze.maze.tagger;

import java.util.ArrayDeque;

import maze.maze.*;
import maze.maze.player.*;

class Coordinate {
  int x;
  int y;
}

public class TaggerSearchModel {
  private MazeModel mazeModel;
  private PlayerModel playerModel;
  private TaggerModel taggerModel;

  private int mazeWidth;
  private int mazeHeight;
  private int[][] dist;
  private Coordinate start;
  private Coordinate goal;
  private final int[] dx = { 1, 0, -1, 0 };
  private final int[] dy = { 0, 1, 0, -1 };
  private final int[] numberOfSteps = { 0 };
  private final Object monitor1 = new Object();

  public TaggerSearchModel(MazeModel mazeModel, PlayerModel playerModel, TaggerModel taggerModel) {
    this.mazeModel = mazeModel;
    this.playerModel = playerModel;
    this.taggerModel = taggerModel;

    this.mazeWidth = mazeModel.getMazeWidth();
    this.mazeHeight = mazeModel.getMazeHeight();

    goal = new Coordinate();
    goal.x = Math.round(playerModel.getPlayerX());
    goal.y = Math.round(playerModel.getPlayerY());

    initializeDistance();
  }

  // * dist[][] を初期化 */
  public void initializeDistance() {
    dist = new int[mazeWidth][mazeHeight];
    for (int x = 0; x < mazeWidth; x++) {
      for (int y = 0; y < mazeHeight; y++) {
        dist[x][y] = -1;
      }
    }
  }

  public ArrayDeque<Coordinate> performBFS() {
    initializeDistance();
    start = new Coordinate();
    start.x = Math.round(taggerModel.getTaggerX());
    start.y = Math.round(taggerModel.getTaggerY());
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
            Coordinate nextElem = new Coordinate();
            nextElem.x = nx;
            nextElem.y = ny;
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

    // * dist[][] 確認用デバック */
    for (int i = 0; i < mazeHeight; i++) {
      for (int j = 0; j < mazeWidth; j++) {
        System.out.printf("%3d", dist[j][i]);
      }
      System.out.print("\n");
    }

    // * プレイヤー位置までの移動が不可能な場合の処理 */
    ArrayDeque<Coordinate> stack = new ArrayDeque<>();
    if (dist[goal.x][goal.y] == -1) {
      System.out.println("プレイヤー位置までの移動が不可能です.");
      return stack;
    }

    stack.add(goal);
    Coordinate elem = goal;
    int ptDistance = dist[goal.x][goal.y];

    // * 目的地までの距離確認用デバック */
    // System.out.printf("Distance: %2d\n", ptDistance);

    while (ptDistance > 0) {
      for (int i = 0; i < 4; i++) {
        int nx = elem.x + dx[i];
        int ny = elem.y + dy[i];

        if (nx >= 0 && nx < mazeWidth && ny >= 0 && ny < mazeHeight && dist[nx][ny] == ptDistance - 1) {
          Coordinate nextElem = new Coordinate();
          nextElem.x = nx;
          nextElem.y = ny;
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
  public void moveToward(ArrayDeque<Coordinate> path) {
    while (!path.isEmpty()) {

      Coordinate next = path.pollLast();

      // * taggerの前の移動が終わる(整数座標になる)まで待つ */
      if (next != null) {
        try {
          waitForCondition1();
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
    }
  }

  // * 条件が達成されるまでずっとまってる関数 */
  public void waitForCondition1() throws InterruptedException {
    synchronized (monitor1) {
      while (!taggerModel.getFlag()) {
        monitor1.wait();
      }
    }
  }

  public void signalConditionMet1() {
    synchronized (monitor1) {
      monitor1.notifyAll();
    }
  }

  // * TaggerModel で呼び出す用のメソッド */
  public void executeTaggerMovement() {
    numberOfSteps[0] = 0;
    ArrayDeque<Coordinate> path = performBFS();
    if (!path.isEmpty()) {
      moveToward(path);
    }

    // numberOfSteps[0] = 0;
    // ArrayDeque<Coordinate> path2 = performBFS();
    // if (!path2.isEmpty()) {
    // moveToward(path2);
    // }
    // }

    // public void waitForCondition2() throws InterruptedException {
    // synchronized (monitor2) {
    // while (numberOfSteps[0] < 5) {
    // monitor2.wait();
    // }
    // }
    // }

    // public void signalConditionMet2() {
    // synchronized (monitor2) {
    // monitor2.notifyAll();
    // }
    // }
  }
}
