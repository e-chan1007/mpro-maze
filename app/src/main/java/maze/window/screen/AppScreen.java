package maze.window.screen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import maze.window.AppWindow;

public abstract class AppScreen extends JPanel {
  public AppScreen() {
    setBackground(Color.BLACK);
    setSize(new Dimension(AppWindow.getScreenWidth(), AppWindow.getScreenHeight()));
  }
}
