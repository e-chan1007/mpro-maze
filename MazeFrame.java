import java.awt.*;
import javax.swing.*;

/**
 * 迷路の盤面を表示するJFrame
 * @see MazeView
 */
public class MazeFrame extends JFrame {
  MazeModel model;

  public MazeFrame() {
    model = new MazeModel();
    this.setBackground(Color.black);
    this.setTitle("Maze");
    this.add(new MazeView(model));
    this.pack();
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
  }
}