package maze.maze.element;

import java.awt.Graphics;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;
import java.util.Random;

import maze.maze.MazeModel;
import maze.maze.player.PlayerModel;

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
<<<<<<< HEAD
    if (!this.isTaskCompleted()) {
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
              setTaskCompleted(true);
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
    JFrame frame = new JFrame("New Window");
    frame.setSize(400, 300);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    MovingPanel panel = new MovingPanel();
    frame.add(panel);

    Timer moveTimer = new Timer(10, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        panel.moveRedLine();
      }
    });
    moveTimer.start();

    frame.setLocation(frame.getX() + 400, frame.getY());

    frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "closeWindow");
    frame.getRootPane().getActionMap().put("closeWindow", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
          if (panel.isRedLineInYellowRange()) {
            System.out.println("success!");
          } else {
            System.out.println("failure...");
            count--;
          }
          frame.dispose();
          moveTimer.stop();
          timer.start();
        }
    });

    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        timer.start(); // ウィンドウが閉じられたらタイマーを再開
      }
    });

    frame.setVisible(true);
  }

  private class MovingPanel extends JPanel {
    private int yellowLineX;
    private int redLineX = 70;
    private int direction = 2;

    public MovingPanel() {
      Random rand = new Random();
      yellowLineX = 130 + rand.nextInt(81);
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, getWidth(), getHeight());
      g.setColor(Color.WHITE);
      g.fillRect(70, 140, 260, 20);
      g.setColor(Color.YELLOW);
      g.fillRect(yellowLineX, 140, 20, 20);
      g.setColor(Color.RED);
      g.fillRect(redLineX, 100, 10, 60);
    }

    public void moveRedLine() {
      redLineX += direction;
      if (redLineX == 320) {
        System.out.println("failure...");
        count--;
        SwingUtilities.getWindowAncestor(this).dispose();
      }
      repaint();
    }

    public boolean isRedLineInYellowRange() {
      return redLineX + 5 >= yellowLineX && redLineX <= yellowLineX + 15;
=======
    if (!this.isCompleted()) {
      this.setCompleted(true);
>>>>>>> 5f65b26d9d7774a1ad3f92cac3ffcef6b691d9de
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