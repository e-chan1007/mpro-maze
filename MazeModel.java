import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * 迷路の盤面を管理するモデル
 */
public class MazeModel extends Observable {
  public static final int MAZE_CELL_SIZE = 48;
  public static final int MAZE_SIZE = 7;

  private MazeElement[][] elements = new MazeElement[MAZE_SIZE][MAZE_SIZE];

  private Map<Character, Class<? extends MazeElement>> elementMap = new HashMap<>(){{
    put('#', WallModel.class);
    put(' ', PathModel.class);
  }};

  public MazeModel() {
    this.readFile("test.txt");
    // for(int x = 0; x < MAZE_SIZE; x++) {
    //   for(int y = 0; y < MAZE_SIZE; y++) {
    //     this.elements[x][y] = new PathModel();
    //   }
    // }
    
    // this.elements[1][1] = new WallModel();
    // this.elements[2][1] = new WallModel();
    // this.elements[3][1] = new WallModel();
  }

  public MazeElement[][] getElements() {
    return this.elements;
  }

  public void readFile(String path) {
    this.elements = new MazeElement[MAZE_SIZE][MAZE_SIZE];
    try {
      List<String> lines = Files.readAllLines(Path.of(path));
      for(int y = 0; y < MAZE_SIZE; y++) {
        if(lines.size() <= y) break;
        String line = lines.get(y);
        for(int x = 0; x < MAZE_SIZE; x++) {          
          if(lines.size() <= x) break;
          char c = line.charAt(x);
          Class<? extends MazeElement> elementClass = elementMap.get(c);
          if(elementClass != null) elements[x][y] = elementClass.getDeclaredConstructor().newInstance();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
