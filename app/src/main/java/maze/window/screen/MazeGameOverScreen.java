package maze.window.screen;

import javax.swing.JLabel;

public class MazeGameOverScreen extends TranslucentScreenBase {
  public MazeGameOverScreen() {
    super();
    setOpacity(0);

    JLabel label = new JLabel("GAME OVER", JLabel.CENTER);
    label.setForeground(getForeground());
    label.setPreferredSize(getSize());
    add(label);
  }
}
