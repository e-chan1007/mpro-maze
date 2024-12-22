package maze.maze.tagger;

import java.util.ArrayDeque;

import maze.maze.MazeModel;

class Coordinate {
  int x;
  int y;
}

public class TaggerSearchModel {
  MazeModel mazeModel = new MazeModel();
  TaggerModel taggerModel = new TaggerModel(mazeModel);

  private int mazeWidth = mazeModel.getMazeWidth();
  private int mazeHeight = mazeModel.getMazeHeight();
  private int[][] dist;

  public TaggerSearchModel(MazeModel mazeModel, TaggerModel taggerModel) {
    this.mazeModel = mazeModel;
    this.taggerModel = taggerModel;

    dist = new int[mazeWidth][mazeHeight];
    for(int x = 0; x < mazeWidth; x++) {
      for(int y = 0; y < mazeHeight; y++) {
        dist[x][y] = -1;
      }
    }

    int[] dx = {1, 0, -1, 0};
    int[] dy = {0, 1, 0, -1};

    Coordinate start = new Coordinate();
    start.x = 4;
    start.y = 4;

    Coordinate goal = new Coordinate();
    goal.x = 1;
    goal.y = 1;

    ArrayDeque<Coordinate> queue = new ArrayDeque<>();
    queue.add(start);
    dist[start.x][start.y] = 0;

    //* プレイヤーの現在地までの距離を計測 */
    while(queue.peek() != null) {
      Coordinate elem = queue.poll();
      for(int i = 0; i < 4; i++) {
        Coordinate nextElem = new Coordinate();
        nextElem.x = elem.x + dx[i];
        nextElem.y = elem.y + dy[i];
        if(dist[nextElem.x][nextElem.y] == -1 && mazeModel.getElementAt(nextElem.x, nextElem.y).canEnter()) {
          queue.add(nextElem);
          dist[nextElem.x][nextElem.y] = dist[elem.x][elem.y] + 1;
        }
      }
    }

    //* プレイヤーまでの道筋を記録 */
    int ptDistance = dist[goal.x][goal.y];
    ArrayDeque<Coordinate> stack = new ArrayDeque<>();
    stack.add(goal);
    Coordinate elem = new Coordinate();
    elem = goal;
    for(; ptDistance >= 0; ptDistance--) {
      for(int j = 0; j < 4; j++) {
        if(dist[elem.x + dx[j]][elem.y + dy[j]] == ptDistance - 1) {
          elem.x += dx[j];
          elem.y += dy[j];
          stack.add(elem);
          break;
        }
      }
    }

    //* 指定位置まで移動 */
    while(stack.peekLast() != null) {
      Coordinate next = stack.pollLast();
      if(next.x == taggerModel.getTaggerX()+1) {
        taggerModel.moveRight();
      } else if(next.x == taggerModel.getTaggerX()-1) {
        taggerModel.moveLeft();
      } else if(next.y == taggerModel.getTaggerY()+1) {
        taggerModel.moveUp();
      } else if(next.y == taggerModel.getTaggerY()-1) {
        taggerModel.moveDown();
      }
    }

  }
}

