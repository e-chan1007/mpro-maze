package maze.maze.tagger;

import java.nio.charset.CoderResult;
import java.util.ArrayDeque;
import java.util.ConcurrentModificationException;
import javax.swing.Timer;
import maze.maze.MazeModel;
import maze.maze.player.PlayerModel;

class Coordinate {
  int x;
  int y;
}

public class TaggerSearchModel {
  private MazeModel mazeModel;
  private TaggerModel taggerModel;

  private int mazeWidth;
  private int mazeHeight;
  private int[][] dist;
  private Coordinate start;
  private Coordinate goal;
  private final int[] dx = { 1, 0, -1, 0 };
  private final int[] dy = { 0, 1, 0, -1 };

  public TaggerSearchModel(MazeModel mazeModel, PlayerModel playerModel, TaggerModel taggerModel) {
    this.mazeModel = mazeModel;
    this.taggerModel = taggerModel;

    this.mazeWidth = mazeModel.getMazeWidth();
    this.mazeHeight = mazeModel.getMazeHeight();

    start = new Coordinate();
    start.x = Math.round(taggerModel.getTaggerX());
    start.y = Math.round(taggerModel.getTaggerY());

    goal = new Coordinate();
    goal.x = Math.round(playerModel.getPlayerX());
    goal.y = Math.round(playerModel.getPlayerY());

    initializeDistance();

    taggerModel.addObserver((observable, arg) -> {
      System.out.println("Tagger位置が更新されました: (" + taggerModel.getTaggerX() + ", " + taggerModel.getTaggerY() + ")");
    });

    this.mazeWidth = mazeModel.getMazeWidth();
    this.mazeHeight = mazeModel.getMazeHeight();

  }

  public void initializeDistance() {
    dist = new int[mazeWidth][mazeHeight];
    for (int x = 0; x < mazeWidth; x++) {
      for (int y = 0; y < mazeHeight; y++) {
        dist[x][y] = -1;
      }
    }
  }

  public ArrayDeque<Coordinate> performBFS() {
    ArrayDeque<Coordinate> queue = new ArrayDeque<>();
    queue.add(start);
    dist[start.x][start.y] = 0;

    // * プレイヤーの現在地までの距離を計測 */
    while (!queue.isEmpty()) {
      Coordinate elem = queue.poll();

      for (int i = 0; i < 4; i++) {
        int nx = elem.x + dx[i];
        int ny = elem.y + dy[i];

        // * 範囲チェック */
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
    }

    // デバッグ: BFS 結果を出力
    System.out.println("Distance Map:");
    for (int y = 0; y < mazeHeight; y++) {
      for (int x = 0; x < mazeWidth; x++) {
        System.out.print(dist[x][y] + " ");
      }
      System.out.println();
    }

    ArrayDeque<Coordinate> stack = new ArrayDeque<>();
    if (dist[goal.x][goal.y] == -1) {
      System.out.println("can not find player");
      return stack;
    }

    stack.add(goal);
    Coordinate elem = goal;
    int ptDistance = dist[goal.x][goal.y];

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

  public void moveAlongPath(ArrayDeque<Coordinate> path) {
    final int[] delay = { 100 }; // タイマーの遅延時間 (ms)
    final Timer timer = new Timer(delay[0], null); // Swing Timer を作成

    timer.addActionListener(e -> {
      if (!path.isEmpty()) {
        Coordinate next = path.pollLast();
        if (next != null) {
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
      } else {
        timer.stop(); // 経路が空になったらタイマーを停止
      }
    });

    timer.start(); // タイマーを開始
  }

  public void executeTaggerMovement() {
    ArrayDeque<Coordinate> path = performBFS();
    if (!path.isEmpty()) {
      moveAlongPath(path);
    }
  }
}
