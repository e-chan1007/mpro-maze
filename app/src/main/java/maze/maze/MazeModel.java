package maze.maze;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import maze.maze.element.*;

/**
 * 迷路の盤面を管理するモデル
 */
public class MazeModel extends maze.util.Observable implements maze.util.Observer {
  private int mazeWidth;
  private int mazeHeight;

  private MazeElement[][] elements;
  private List<TaskElement> tasks;
  private MazeView view;

  private Map<Character, Class<? extends MazeElement>> elementMap = new HashMap<>() {
    {
      put('#', WallModel.class);
      put(' ', PathModel.class);
      put('G', GoalModel.class);
      put('S', StartModel.class);
      put('1', CollectTaskModel.class);
    }
  };

  public MazeModel() {
    this.readFile("/test.txt");
  }

  protected void setView(MazeView view) {
    this.view = view;
  }

  /**
   * 座標が迷路の範囲内かどうか
   *
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
   *
   * @param x X座標
   * @param y Y座標
   */
  public MazeElement getElementAt(int x, int y) {
    return this.elements[x][y];
  }

  /**
   * 迷路がゴール可能かどうか(すべてのタスクが完了しているか)
   */
  public boolean canGoal() {
    return this.tasks.isEmpty();
  }

  /**
   * 特定クラスの要素が存在する座標を取得する
   *
   * @param c クラス
   * @return 存在時は int[] [x, y]、存在しない場合は null
   */
  public int[] locateElement(Class<? extends MazeElement> c) {
    for (int x = 0; x < mazeWidth; x++) {
      for (int y = 0; y < mazeHeight; y++) {
        MazeElement element = this.elements[x][y];
        if (c.isInstance(element))
          return new int[] { x, y };
      }
    }
    return null;
  }

  /* 迷路サイズを動的に取得，作成 */
  public void readFile(String path) {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(path)))) {
      List<String> lines = reader.lines().collect(Collectors.toList());
      int height = lines.size();
      int width = lines.stream().mapToInt(String::length).max().orElse(0);

      this.mazeWidth = width;
      this.mazeHeight = height;
      this.elements = new MazeElement[width][height];
      this.tasks = new ArrayList<TaskElement>();

      for (int y = 0; y < height; y++) {
        String line = lines.get(y);
        for (int x = 0; x < line.length(); x++) {
          char c = line.charAt(x);
          Class<? extends MazeElement> elementClass = elementMap.get(c);
          if (elementClass != null) {
            MazeElement element = elementClass.getDeclaredConstructor().newInstance();
            element.addObserver(this);
            this.elements[x][y] = element;
            if (element instanceof TaskElement) {
              this.tasks.add((TaskElement) element);
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    setChanged();
    notifyObservers();
  }

  @Override
  public void update(maze.util.Observable observable, Object object) {
    if (observable instanceof TaskElement) {
      TaskElement task = (TaskElement) observable;
      if (task.isTaskCompleted()) {
        this.tasks.remove(task);
        if (this.tasks.isEmpty()) {
          System.out.println("All tasks are completed!");
        }
      }
    }
  }
}
