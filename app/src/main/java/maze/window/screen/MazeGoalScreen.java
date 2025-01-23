package maze.window.screen;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;

import maze.maze.MazeModel;
import maze.window.AppScreenManager;

public class MazeGoalScreen extends TranslucentScreenBase {
  public MazeGoalScreen(MazeModel mazeModel) {
    super();
    setOpacity(0);

    JLabel label = new JLabel("GOAL!", JLabel.CENTER);
    label.setForeground(getForeground());
    label.setPreferredSize(getSize());
    add(label);

    mazeModel.setPaused(true);

    // TODO: 仮コードなので本番は削除すべき
    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
          case KeyEvent.VK_ESCAPE -> {
            AppScreenManager.getInstance().pop();
          }
        }
      }
    });
  }
}
