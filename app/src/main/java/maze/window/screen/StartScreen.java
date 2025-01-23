package maze.window.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import maze.asset.ImageManager;
import maze.ui.Menu;
import maze.window.AppScreenManager;

public class StartScreen extends ScreenBase {
  private final BufferedImage bgImage = ImageManager.loadImage("/bg.png");

  public StartScreen() {
    super();
    setBackground(Color.BLACK);
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setAlignmentX(CENTER_ALIGNMENT);
    setAlignmentY(CENTER_ALIGNMENT);
    add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 1080), new Dimension(0, 1080)));
    add(new Logo());
    add(Box.createVerticalStrut(20));
    add(
        new Menu()
            .add("Start", () -> {
              AppScreenManager.getInstance().push(new MazePlayScreen());
            })
            .add("Exit", () -> {
              System.exit(0);
            })
            .build());
    add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 1080), new Dimension(0, 1080)));

  }

  @Override
  public void draw(Graphics2D g) {
    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
  }

  private class Logo extends JPanel {
    private final BufferedImage image = ImageManager.loadImage("/logo.png");

    public Logo() {
      setBackground(new Color(0, 0, 0, 0));
      setOpaque(true);
      setFocusable(false);
      setPreferredSize(new Dimension(1920, 400));
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.drawImage(image, (1920 - 480) / 2, 0, 480, 160, this);
    }
  }
}
