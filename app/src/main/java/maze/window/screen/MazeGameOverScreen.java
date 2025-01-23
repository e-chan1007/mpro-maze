package maze.window.screen;

import javax.swing.JLabel;

import maze.maze.MazeModel;

public class MazeGameOverScreen extends TranslucentScreenBase {
  public MazeGameOverScreen(MazeModel mazeModel) {
    super();
    setOpacity(0);

    mazeModel.setPaused(true);

    JLabel label = new JLabel("GAME OVER", JLabel.CENTER);
    label.setForeground(getForeground());
    label.setPreferredSize(getSize());
    add(label);
  }
}
