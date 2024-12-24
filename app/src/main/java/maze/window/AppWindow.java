package maze.window;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import maze.util.Observable;
import maze.window.screen.AppScreen;
import maze.window.screen.FadeScreen;
import maze.window.screen.MazeScreen;

public class AppWindow extends JFrame {
  AppScreenManager screenManager = AppScreenManager.getInstance();
  private static int innerWidth = 800;
  private static int innerHeight = 600;

  public AppWindow() {
    setTitle("Maze");
    setSize(innerWidth, innerHeight);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JLayeredPane layeredPane = new JLayeredPane();

    int l[] = { 0 };
    screenManager.addObserver((Observable o, Object arg) -> {
      List<AppScreen> visibleScreens = screenManager.getScreensAsList();
      List<Component> currentScreens = Arrays.asList(layeredPane.getComponents());
      currentScreens.stream().filter(s -> s instanceof AppScreen && !visibleScreens.contains((AppScreen) s))
          .forEach(s -> {
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
    screenManager.push(new FadeScreen(FadeScreen.Fader.FADE_IN));

    add(layeredPane);
    setVisible(true);

    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        innerWidth = getWidth() - getInsets().left - getInsets().right;
        innerHeight = getHeight() - getInsets().top - getInsets().bottom;
        screenManager.getScreensAsList().forEach(s -> {
          s.setSize(new Dimension(innerWidth, innerHeight));
        });
      }
    });
  }

  public static int getInnerWidth() {
    return innerWidth;
  }

  public static int getInnerHeight() {
    return innerHeight;
  }
}
