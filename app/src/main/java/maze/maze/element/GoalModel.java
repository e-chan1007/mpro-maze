package maze.maze.element;

import java.awt.Color;
import java.awt.Graphics;

import maze.maze.MazeModel;
import maze.maze.player.PlayerModel;
import maze.window.AppScreenManager;
import maze.window.screen.MazeGoalScreen;

public class GoalModel extends MazeElement {
  public GoalModel(MazeModel mazeModel, PlayerModel playerModel) {
    super(mazeModel, playerModel);
  }

  @Override
  public void onEnter() {
    if (mazeModel.canGoal()) {
      System.out.println("Goal!");
      AppScreenManager.getInstance().push(new MazeGoalScreen());
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
