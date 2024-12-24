package maze.window.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyListener;

import maze.window.AppScreenManager;

public class TestOverlayScreen extends AppScreen {
  public TestOverlayScreen(Color c) {
    // setSize(500, 500);
    setFocusable(true);
    setOpaque(false);

    addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(java.awt.event.KeyEvent e) {
        System.out.println("keyTyped");
      }

      @Override
      public void keyPressed(java.awt.event.KeyEvent e) {
        System.out.println("keyPressed");
        AppScreenManager.getInstance().pop();
      }

      @Override
      public void keyReleased(java.awt.event.KeyEvent e) {
        System.out.println("keyReleased");
      }
    });
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(new Color(0, 0, 0, 0.5f));
    g.fillRect(0, 0, 800, 600);
  }
}
