package maze.window.screen;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.Timer;

import maze.maze.MazeModel;
import maze.window.AppScreenManager;

public class MazePauseScreen extends TranslucentScreenBase {
  public MazePauseScreen(MazeModel mazeModel) {
    super();
    setOpacity(0);

    JLabel label = new JLabel("PAUSE", JLabel.CENTER);
    label.setForeground(getForeground());
    label.setPreferredSize(getSize());
    add(label);

    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
          case KeyEvent.VK_ESCAPE -> {
            new Timer(1, (ActionEvent e2) -> {
              if (getBackgroundOpacity() == 0 && getForegroundOpacity() == 0) {
                ((Timer) e2.getSource()).stop();
                mazeModel.setPaused(false);
              }
            }).start();
            AppScreenManager.getInstance().pop();
          }
        }
      }
    });
  }
}
