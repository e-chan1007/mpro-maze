package maze.window.screen;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.Timer;

import maze.util.Fader;
import maze.window.AppWindow;

public abstract class ScreenBase extends JPanel {
  private Timer fadeTimer = null;
  private float bgOpacity = 1f;
  private float fgOpacity = 1f;
  private float maxBgOpacity = 1f;
  private float maxFgOpacity = 1f;

  public ScreenBase() {
    setOpaque(false);
    setSize(new Dimension(AppWindow.getInnerWidth(), AppWindow.getInnerHeight()));
  }

  public void draw(Graphics2D g) {
  };

  @Override
  public final void paintComponent(Graphics g) {
    // super.paintComponent(g);
    Color bgColor = getBackground();
    g.setColor(
        new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), (int) (bgColor.getAlpha() * bgOpacity)));
    g.fillRect(0, 0, getWidth(), getHeight());
    ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fgOpacity));
    draw((Graphics2D) g);
  }

  public final void setBackgroundOpacity(float opacity) {
    bgOpacity = opacity;
    repaint();
  }

  public final void setForegroundOpacity(float opacity) {
    fgOpacity = opacity;
    repaint();
  }

  public final void setOpacity(float opacity) {
    bgOpacity = opacity;
    fgOpacity = opacity;
    repaint();
  }

  public final void setMaxBackgroundOpacity(float opacity) {
    maxBgOpacity = opacity;
    if (bgOpacity > maxBgOpacity) {
      bgOpacity = maxBgOpacity;
    }
  }

  public final void setMaxForegroundOpacity(float opacity) {
    maxFgOpacity = opacity;
    if (fgOpacity > maxFgOpacity) {
      fgOpacity = maxFgOpacity;
    }
  }

  public final float getBackgroundOpacity() {
    return bgOpacity;
  }

  public final float getForegroundOpacity() {
    return fgOpacity;
  }

  public final void fadeOut(Runnable onFinished) {
    final float initialBgOpacity = bgOpacity;
    final float initialFgOpacity = fgOpacity;

    if (fadeTimer != null) {
      fadeTimer.stop();
    }
    fadeTimer = Fader.FADE_OUT.createTimer(v -> {
      bgOpacity = initialBgOpacity * v;
      fgOpacity = initialFgOpacity * v;
      repaint();
    }, () -> {
      fadeTimer = null;
      if (onFinished != null)
        onFinished.run();
    });
    fadeTimer.start();
  }

  public final void fadeIn(Runnable onFinished) {
    final float initialBgOpacity = bgOpacity;
    final float initialFgOpacity = fgOpacity;

    if (fadeTimer != null) {
      fadeTimer.stop();
    }
    fadeTimer = Fader.FADE_IN.createTimer(v -> {
      bgOpacity = initialBgOpacity + (maxBgOpacity - initialBgOpacity) * v;
      fgOpacity = initialFgOpacity + (maxFgOpacity - initialFgOpacity) * v;
      repaint();
    }, () -> {
      fadeTimer = null;
      if (onFinished != null)
        onFinished.run();
    });
    fadeTimer.start();
  }
}
