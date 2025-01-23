package maze.maze.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import maze.asset.ImageManager;
import maze.maze.MazeModel;
import maze.maze.player.PlayerModel;
import maze.window.AppScreenManager;
import maze.window.screen.TranslucentScreenBase;

public class CollectTaskModel extends TaskElement {
  private Timer timer;
  private int count = 0;
  private static final int COUNT = 16;
  private PlayerModel playerModel;
  private int ovalX, ovalY;
  private int window_count;
  private int window_count2;
  private boolean windowShown = false;
  private boolean windowShown2 = false;
  private Random random = new Random();
  private BufferedImage pathSprite;

  private final BufferedImage INITIAL_IMAGE = ImageManager.DUNGEON_SPRITE.getImageAt(6, 9);
  private final BufferedImage DONE_IMAGE = ImageManager.DUNGEON_SPRITE.getImageAt(5, 9);

  public CollectTaskModel(MazeModel mazeModel, PlayerModel playerModel) {
    this.timer = new Timer(500, null);
    this.playerModel = playerModel;
    this.window_count = 2 + random.nextInt(5);
    this.window_count2 = 10 + random.nextInt(5);
  }

  @Override
  public void onAllInitiated(MazeModel mazeModel, int x, int y) {
    this.ovalX = x;
    this.ovalY = y;

    boolean isTopWall = mazeModel.getElementAt(x, y - 1) instanceof WallModel;
    boolean isLeftWall = mazeModel.getElementAt(x - 1, y) instanceof WallModel;
    boolean isRightWall = mazeModel.getElementAt(x + 1, y) instanceof WallModel;
    boolean isBottomWall = mazeModel.getElementAt(x, y + 1) instanceof WallModel;

    if (!(isTopWall || isBottomWall || isLeftWall || isRightWall)) {
      this.pathSprite = ImageManager.DUNGEON_SPRITE.getRandomImage(7, 0, 10, 3);
    } else if (isTopWall) {
      if (isLeftWall) {
        this.pathSprite = ImageManager.DUNGEON_SPRITE.getImageAt(1, 1);
      } else if (isRightWall) {
        this.pathSprite = ImageManager.DUNGEON_SPRITE.getImageAt(4, 1);
      } else {
        this.pathSprite = ImageManager.DUNGEON_SPRITE.getRandomImage(2, 1, 3, 1);
      }
    } else if (isBottomWall) {
      this.pathSprite = ImageManager.DUNGEON_SPRITE.getRandomImage(2, 3, 3, 3);
    } else if (isLeftWall) {
      this.pathSprite = ImageManager.DUNGEON_SPRITE.getImageAt(1, 2);
    } else {
      this.pathSprite = ImageManager.DUNGEON_SPRITE.getImageAt(4, 2);
    }
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
            if (count == window_count && !windowShown) {
              showNewWindow();
              windowShown = true;
              timer.stop();
            }
            if (count == window_count2 && !windowShown2) {
              showNewWindow();
              windowShown2 = true;
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
    private int redLineX = 0;
    private int direction = 6;
    private final int anchorX = 1920 / 2 - 250 / 2;
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
      g.fillRect(anchorX, anchorY + 140, 250, 20);
      g.setColor(Color.YELLOW);
      g.fillRect(anchorX + yellowLineX, anchorY + 140, 20, 20);
      g.setColor(Color.RED);
      g.fillRect(anchorX + redLineX, anchorY + 100, 10, 60);
    }

    public void moveRedLine() {
      redLineX += direction;
      if (redLineX >= 240) {
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
    g.drawImage(pathSprite, x, y, size, size, null);

    int torchSize = size / 3 * 2;
    int torchX = x + size / 2 - torchSize / 2;
    int torchY = y + size / 2 - torchSize / 2;
    if (this.isCompleted()) {
      g.drawImage(DONE_IMAGE, torchX, torchY, torchSize, torchSize, null);
    } else {
      g.drawImage(INITIAL_IMAGE, torchX, torchY, torchSize, torchSize, null);
    }
  }
}
