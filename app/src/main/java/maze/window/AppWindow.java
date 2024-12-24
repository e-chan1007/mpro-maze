package maze.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.OverlayLayout;

import maze.util.*;
import maze.window.screen.*;

public class AppWindow extends JFrame {
  AppScreenManager screenManager = AppScreenManager.getInstance();
  private static int screenWidth = 800;
  private static int screenHeight = 600;

  public AppWindow() {
    setTitle("Maze");
    setSize(screenWidth, screenHeight);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // setResizable(false);

    JLayeredPane layeredPane = new JLayeredPane();

    int l[] = { 0 };
    screenManager.addObserver((Observable o, Object arg) -> {
      List<AppScreen> visibleScreens = screenManager.getScreensAsList();
      List<Component> currentScreens = Arrays.asList(layeredPane.getComponents());
      currentScreens.stream().filter(s -> s instanceof AppScreen && !visibleScreens.contains(s)).forEach(s -> {
        layeredPane.remove(s);
        l[0]--;
      });
      visibleScreens.stream().filter(s -> !currentScreens.contains(s)).forEach(s -> {
        layeredPane.add(s);
        layeredPane.setLayer(s, l[0]++);
      });
      revalidate();
      repaint();
      transferFocus();
    });

    screenManager.push(new MazeScreen());
    screenManager.push(new FadeScreen());

    add(layeredPane);
    setVisible(true);

    addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        screenWidth = getWidth();
        screenHeight = getHeight();
        screenManager.getScreensAsList().forEach(s -> {
          s.setSize(new Dimension(screenWidth, screenHeight));
        });
      }
    });
  }

  public static int getScreenWidth() {
    return screenWidth;
  }

  public static int getScreenHeight() {
    return screenHeight;
  }
}
