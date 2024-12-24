package maze.window.screen;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.Timer;

import maze.window.AppScreenManager;

public class FadeScreen extends AppScreen {
  float opacity = 0f;

  public FadeScreen() {
    setOpaque(false);
    Timer fadeOut = new Timer(1, e -> {
      opacity -= 0.02f;
      if (opacity <= 0) {
        ((Timer) e.getSource()).stop();
        System.out.println("Fade out done");
        AppScreenManager.getInstance().pop();
      }
      repaint();
    });
    Timer fadeIn = new Timer(1, e -> {
      opacity += 0.02f;
      if (opacity >= 1) {
        ((Timer) e.getSource()).stop();
        fadeOut.start();
        System.out.println("Fade out");
      }
      repaint();
    });
    fadeIn.start();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    System.out.println("Painting fade screen" + opacity);
    g.setColor(new Color(0, 0, 0, Math.clamp(opacity, 0.0f, 1.0f)));
    g.fillRect(0, 0, getWidth(), getHeight());
  }
}
