package maze.window.screen;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.Timer;

import maze.maze.MazeModel;
import maze.ui.Menu;
import maze.window.AppScreenManager;

public class MazePauseScreen extends TranslucentScreenBase {
  public MazePauseScreen(MazeModel mazeModel) {
    super();

    setOpacity(0f);
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setAlignmentX(CENTER_ALIGNMENT);
    setAlignmentY(CENTER_ALIGNMENT);
    add(Box.createVerticalGlue());

    JLabel label = new JLabel("PAUSE");
    label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
    label.setForeground(getForeground());
    add(label);

    add(Box.createVerticalStrut(20));
    add(
        new Menu()
            .add("Continue", () -> {
              mazeModel.setPaused(false);
              new Timer(1, (ActionEvent e) -> {
                if (getBackgroundOpacity() == 0 && getForegroundOpacity() == 0) {
                  ((Timer) e.getSource()).stop();
                }
              }).start();
              AppScreenManager.getInstance().pop();
            })
            .add("Exit", () -> {
              AppScreenManager.getInstance().pop();
              AppScreenManager.getInstance().pop();
            })
            .build());
    add(Box.createVerticalGlue());
  }
}
