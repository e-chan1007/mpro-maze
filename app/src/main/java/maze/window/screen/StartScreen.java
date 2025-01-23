package maze.window.screen;

import java.awt.Color;

import javax.swing.Box;
import javax.swing.BoxLayout;

import maze.ui.Menu;
import maze.window.AppScreenManager;

public class StartScreen extends ScreenBase {
  public StartScreen() {
    super();
    setBackground(Color.BLACK);
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setAlignmentX(CENTER_ALIGNMENT);
    setAlignmentY(CENTER_ALIGNMENT);

    add(Box.createVerticalGlue());
    add(
        new Menu()
            .add("Start", () -> {
              AppScreenManager.getInstance().push(new MazePlayScreen());
            })
            .add("Exit", () -> {
              System.exit(0);
            })
            .build());
    add(Box.createVerticalGlue());
  }
}
