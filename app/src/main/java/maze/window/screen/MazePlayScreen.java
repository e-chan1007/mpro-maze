package maze.window.screen;

import java.awt.event.KeyAdapter;

import maze.maze.MazeModel;
import maze.maze.MazeView;
import maze.maze.player.PlayerController;
import maze.maze.player.PlayerModel;
import maze.window.AppScreenManager;

/**
 * 迷路の盤面を表示する
 *
 * @see MazeView
 */
public class MazePlayScreen extends ScreenBase {
  MazeView mazeView;

  public MazePlayScreen() {
    MazeModel mazeModel = new MazeModel();
    PlayerModel playerModel = new PlayerModel(mazeModel);

    mazeView = new MazeView(mazeModel, playerModel);
    PlayerController playerController = new PlayerController(playerModel);

    playerModel.addObserver(mazeView);
    mazeView.addKeyListener(playerController);

    add(mazeView);

    mazeView.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(java.awt.event.KeyEvent e) {
        switch (e.getKeyCode()) {
          case java.awt.event.KeyEvent.VK_ESCAPE -> {
            mazeModel.setPaused(true);
            AppScreenManager.getInstance().push(new MazePauseScreen(mazeModel));
          }
        }
      }
    });
  }

  @Override
  public void requestFocus() {
    mazeView.requestFocus();
  }
}
