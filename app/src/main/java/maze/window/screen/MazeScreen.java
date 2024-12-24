package maze.window.screen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import maze.maze.*;
import maze.maze.player.*;

/**
 * 迷路の盤面を表示する
 *
 * @see MazeView
 */
public class MazeScreen extends AppScreen {
  MazeView mazeView;

  public MazeScreen() {
    MazeModel mazeModel = new MazeModel();
    PlayerModel playerModel = new PlayerModel(mazeModel);

    mazeView = new MazeView(mazeModel, playerModel);
    PlayerController playerController = new PlayerController(playerModel);

    playerModel.addObserver(mazeView);
    mazeView.addKeyListener(playerController);

    add(mazeView);
  }
}
