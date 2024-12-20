import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * 迷路の盤面を管理するモデル
 */
public class MazeModel extends Observable {
  public static final int MAZE_CELL_SIZE = 48;
  // public static final int MAZE_SIZE = 7;
  private int mazeWidth;
  private int mazeHeight;

  private MazeElement[][] elements;

  private Map<Character, Class<? extends MazeElement>> elementMap = new HashMap<>(){{
    put('#', WallModel.class);
    put(' ', PathModel.class);
    put('G', GoalModel.class);
  }};

  public MazeModel() {
    this.readFile("test.txt");
  }
  /**
   * 座標が迷路の範囲内かどうか
   * @param x X座標
   * @param y Y座標
   */
  public boolean isInMaze(int x, int y) {
    return 0 <= x && 0 <= y && x < mazeWidth && y < mazeHeight;
  }

  public MazeElement[][] getElements() {
    return this.elements;
  }

  public int getMazeWidth() {
    return mazeWidth;
  }

  public int getMazeHeight() {
    return mazeHeight;
  }

  /**
   * 特定の座標の迷路要素を取得する
   * @param x X座標
   * @param y Y座標
   */
  public MazeElement getElementAt(int x, int y) {
    return this.elements[x][y];
  }

  //* 迷路サイズを動的に取得，作成 */
  public void readFile(String path) {
    // this.elements = new MazeElement[MAZE_SIZE][MAZE_SIZE];
    try {
      List<String> lines = Files.readAllLines(Path.of(path));
      int height = lines.size();
      int width = lines.stream().mapToInt(String::length).max().orElse(0);

      this.mazeWidth = width;
      this.mazeHeight = height;
      this.elements = new MazeElement[width][height];

      for(int y = 0; y < height; y++) {
        // if(lines.size() <= y) break;
        String line = lines.get(y);
        for(int x = 0; x < line.length(); x++) {          
          // if(lines.size() <= x) break;
          char c = line.charAt(x);
          Class<? extends MazeElement> elementClass = elementMap.get(c);
          if(elementClass != null) {
            elements[x][y] = elementClass.getDeclaredConstructor().newInstance();
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
