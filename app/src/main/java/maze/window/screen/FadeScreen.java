package maze.window.screen;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Timer;

import maze.window.AppScreenManager;

public class FadeScreen extends AppScreen {
  float opacity = 0f;

  public FadeScreen(Fader fader) {
    this(fader, () -> AppScreenManager.getInstance().pop());
  }

  public FadeScreen(Fader fader, Runnable onFinished) {
    setOpaque(false);
    opacity = fader.getInitialOpacity();
    Timer timer = new Timer(1, e -> {
      opacity += 0.01f * fader.getSign();
      if (opacity < 0 || opacity > 1) {
        ((Timer) e.getSource()).stop();
        onFinished.run();
      }
      repaint();
    });

    timer.start();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Color.BLACK);
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
    g2d.fillRect(0, 0, getWidth(), getHeight());
  }

  public static class Fader {
    public static final Fader FADE_IN = new Fader(-1, 1);
    public static final Fader FADE_OUT = new Fader(1, 0);

    private final int sign;
    private final float initialOpacity;

    private Fader(int sign, float initialOpacity) {
      this.sign = sign;
      this.initialOpacity = initialOpacity;
    }

    public int getSign() {
      return sign;
    }

    public float getInitialOpacity() {
      return initialOpacity;
    }
  }
}
