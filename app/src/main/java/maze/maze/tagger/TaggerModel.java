package maze.maze.tagger;

import maze.maze.*;
import maze.util.*;

public class TaggerModel extends Observable {
  private float taggerX = 4;
  private float taggerY = 4;
  private final int STEPS = 30;
  private final int DELAY = 1;
  MazeModel mazeModel = new MazeModel();

  public TaggerModel(MazeModel mazeModel) {
    this.mazeModel = mazeModel;
  }

  public float getTaggerX() {
    return taggerX;
  }

  public float getTaggerY() {
    return taggerY;
  }
}