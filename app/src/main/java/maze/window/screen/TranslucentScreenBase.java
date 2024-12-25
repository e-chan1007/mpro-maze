package maze.window.screen;

import java.awt.Color;

public abstract class TranslucentScreenBase extends ScreenBase {
  public TranslucentScreenBase() {
    setForeground(Color.WHITE);
    setBackground(Color.BLACK);
    setMaxBackgroundOpacity(0.5f);
  }
}
