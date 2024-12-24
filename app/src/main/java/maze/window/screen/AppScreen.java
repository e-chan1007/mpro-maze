package maze.window.screen;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import maze.window.AppWindow;

public abstract class AppScreen extends JPanel {
  public AppScreen() {
    setBackground(Color.BLACK);
    setSize(new Dimension(AppWindow.getInnerWidth(), AppWindow.getInnerHeight()));
  }
}
