/**
 * 迷路の盤面を管理するモデル
 */
public class MazeModel extends Observable {
  public static final int MAZE_CELL_SIZE = 48;
  public static final int MAZE_SIZE = 5;

  private MazeElement[][] elements = new MazeElement[MAZE_SIZE][MAZE_SIZE];

  private int[][] maze = {
      { 1, 1, 1, 1, 1 },
      { 1, 0, 0, 0, 1 },
      { 1, 0, 0, 0, 1 },
      { 1, 0, 0, 0, 1 },
      { 1, 1, 1, 1, 1 },
  };

  public MazeModel() {
    for (int x = 0; x < MAZE_SIZE; x++) {
      for (int y = 0; y < MAZE_SIZE; y++) {
        // this.elements[x][y] = new PathModel();
        if (maze[y][x] == 0) {
          this.elements[x][y] = new PathModel();
        } else {
          this.elements[x][y] = new WallModel();
        }
      }
    }

    // this.elements[1][1] = new WallModel();
    // this.elements[2][1] = new WallModel();
    // this.elements[3][1] = new WallModel();
  }

  public MazeElement[][] getElements() {
    return this.elements;
  }
}
