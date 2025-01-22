package maze.maze.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import maze.maze.MazeModel;
import maze.maze.player.PlayerModel;
import maze.window.AppScreenManager;
import maze.window.screen.TranslucentScreenBase;

public class CollectTaskModel extends TaskElement {
  private Timer timer;
  private int count = 0;
  private static final int COUNT = 10;
  private int window_count;
  private PlayerModel playerModel;
  private int ovalX, ovalY;
  private boolean windowShown = false;
  private Random random = new Random();

  public CollectTaskModel(MazeModel mazeModel, PlayerModel playerModel) {
    this.timer = new Timer(500, null);
    this.playerModel = playerModel;
    this.window_count = 2 + random.nextInt(7);
  }

  @Override
  public void onAllInitiated(MazeModel mazeModel, int x, int y) {
    this.ovalX = x;
    this.ovalY = y;
  }

  @Override
  public void onEnter() {
    if (!this.isCompleted()) {
      timer.addActionListener(new ActionListener() {
        private boolean isOnOval = false;

        public void actionPerformed(ActionEvent e) {
          float playerX = playerModel.getPlayerX();
          float playerY = playerModel.getPlayerY();
          if (Math.abs(playerX - ovalX) < 0.01 && Math.abs(playerY - ovalY) < 0.01) {
            count++;
            isOnOval = true;
            if ((count == window_count || count == COUNT - 2) && !windowShown) {
              showNewWindow();
              windowShown = true;
              timer.stop();
            }
            if (count == COUNT) {
              setCompleted(true);
              System.out.println("The task is completed.");
              timer.stop();
            }
          } else if (isOnOval) {
            timer.stop();
          }
        }
      });
      timer.start();
    }
  }

  private void showNewWindow() {
    AppScreenManager.getInstance().push(new MovingScreen());
  }

  private class MovingScreen extends TranslucentScreenBase {
    private int yellowLineX;
    private int redLineX = 70;
    private int direction = 2;
    private final int anchorX = 1920 / 2 - 300 / 2;
    private final int anchorY = 1080 / 2 - 160 / 2;
    private final Timer moveTimer;

    public MovingScreen() {
      setOpacity(0);
      setBounds(0, 0, 1920, 1080);
      Random rand = new Random();
      yellowLineX = 130 + rand.nextInt(81);

      moveTimer = new Timer(1000 / 60, (ActionEvent e) -> {
        moveRedLine();
      });
      moveTimer.start();

      getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
          KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0),
          "closeWindow");
      getActionMap().put("closeWindow", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
          if (AppScreenManager.getInstance().peek() != MovingScreen.this)
            return;
          if (isRedLineInYellowRange()) {
            System.out.println("success!");
          } else {
            System.out.println("failure...");
            count--;
          }
          AppScreenManager.getInstance().pop();
          moveTimer.stop();
          timer.start();
        }
      });
    }

    @Override
    public void onHide() {
      timer.start();
    }

    @Override
    public void draw(Graphics2D g) {
      g.setColor(Color.WHITE);
      g.fillRect(anchorX, anchorY + 140, 300, 20);
      g.setColor(Color.YELLOW);
      g.fillRect(anchorX + yellowLineX, anchorY + 140, 20, 20);
      g.setColor(Color.RED);
      g.fillRect(anchorX + redLineX, anchorY + 100, 10, 60);
    }

    public void moveRedLine() {
      redLineX += direction;
      if (redLineX >= 320) {
        System.out.println("failure...");
        moveTimer.stop();
        count--;
        if (AppScreenManager.getInstance().peek() == this) {
          AppScreenManager.getInstance().pop();
        }
      }
    }

    public boolean isRedLineInYellowRange() {
      return redLineX + 5 >= yellowLineX && redLineX <= yellowLineX + 15;
    }
  }

  @Override
  public void draw(Graphics g, int x, int y, int size) {
    if (this.isCompleted()) {
      g.setColor(java.awt.Color.GREEN);
    } else {
      g.setColor(java.awt.Color.ORANGE);
    }
    g.fillOval(x, y, size, size);
  }
}
