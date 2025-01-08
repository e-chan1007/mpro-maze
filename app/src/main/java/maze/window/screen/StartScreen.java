package maze.window.screen;

import javax.swing.JButton;

import maze.window.AppScreenManager;

public class StartScreen extends ScreenBase {
  public StartScreen() {
    super();

    JButton startButton = new JButton("Start");
    startButton.addActionListener(e -> {
      AppScreenManager.getInstance().push(new MazePlayScreen());
    });
    add(startButton);
  }
}
