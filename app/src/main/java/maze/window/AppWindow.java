package maze.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import maze.util.Observable;
import maze.window.screen.ScreenBase;
import maze.window.screen.StartScreen;

public class AppWindow extends JFrame {
  private AppScreenManager screenManager = AppScreenManager.getInstance();
  private static int innerWidth = 1920;
  private static int innerHeight = 1080;

  public AppWindow() {
    setUndecorated(true);
    setVisible(true);
    setSize(innerWidth, innerHeight);
    setBackground(Color.BLACK);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice gd = ge.getDefaultScreenDevice();
    // gd.setFullScreenWindow(this);
    // gd.setDisplayMode(Arrays.stream(gd.getDisplayModes())
    // .filter(dm -> dm.getWidth() == innerWidth && dm.getHeight() ==
    // innerHeight).findFirst().get());

    revalidate();

    JLayeredPane layeredPane = new JLayeredPane();

    int l[] = { 0 };
    screenManager.addObserver((Observable o, Object arg) -> {
      List<ScreenBase> visibleScreens = screenManager.getScreensAsList();
      List<Component> currentScreens = Arrays.asList(layeredPane.getComponents());
      currentScreens.stream().filter(s -> s instanceof ScreenBase && !visibleScreens.contains((ScreenBase) s))
          .forEach(s -> {
            ((ScreenBase) s).fadeOut(() -> {
              layeredPane.remove(s);
              ((ScreenBase) s).onHide();
              l[0]--;
            });
          });
      visibleScreens.stream().filter(s -> !currentScreens.contains(s)).forEach(s -> {
        if (l[0] == 0) {
          s.setOpacity(1);
          layeredPane.add(s);
        } else {
          s.setOpacity(0);
          layeredPane.add(s);
          s.fadeIn(null);
        }
        layeredPane.setLayer(s, l[0]++);
      });
      screenManager.peek().requestFocus();
    });

    screenManager.push(new StartScreen());

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

    new WindowUpdateWorker(this).execute();
  }

  public static int getInnerWidth() {
    return innerWidth;
  }

  public static int getInnerHeight() {
    return innerHeight;
  }
}
