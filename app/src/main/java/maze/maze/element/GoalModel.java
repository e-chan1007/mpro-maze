package maze.maze.element;

import java.awt.Color;
import java.awt.Graphics;

import maze.maze.MazeModel;
import maze.window.AppScreenManager;
import maze.window.screen.MazeGoalScreen;

public class GoalModel extends MazeElement {
  private final MazeModel mazeModel;

  public GoalModel(MazeModel mazeModel) {
    this.mazeModel = mazeModel;
  }

  @Override
  public void onEnter() {
    if (mazeModel.canGoal()) {
      System.out.println("Goal!");
      AppScreenManager.getInstance().push(new MazeGoalScreen(mazeModel));
    } else {
      System.out.println("Not all tasks are completed.");
    }
  }

  @Override
  public void draw(Graphics g, int x, int y, int size) {
    if (mazeModel.canGoal()) {
      g.setColor(Color.CYAN);
    } else {
      g.setColor(Color.BLUE);
    }
    g.fillRect(x, y, size, size);
  }
}
