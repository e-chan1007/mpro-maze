package maze.window.screen;

import java.awt.event.KeyAdapter;

import maze.maze.MazeModel;
import maze.maze.MazeView;
import maze.maze.player.PlayerController;
import maze.maze.player.PlayerModel;
import maze.maze.tagger.TaggerModel;
import maze.maze.tagger.TaggerSearchModel;
import maze.window.AppScreenManager;

/**
 * 迷路の盤面を表示する
 *
 * @see MazeView
 */
public class MazePlayScreen extends ScreenBase {
  MazeView mazeView;
  TaggerSearchModel searchModel;

  public MazePlayScreen() {
    MazeModel mazeModel = new MazeModel();
    PlayerModel playerModel = new PlayerModel(mazeModel);
    TaggerModel taggerModel = new TaggerModel(mazeModel);
    searchModel = new TaggerSearchModel(mazeModel, playerModel, taggerModel);
    taggerModel.setSearchModel(searchModel);
    mazeView = new MazeView(mazeModel, playerModel, taggerModel);
    PlayerController playerController = new PlayerController(playerModel);

    playerModel.addObserver(mazeView);
    taggerModel.addObserver(mazeView);
    mazeView.addKeyListener(playerController);

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

    add(mazeView);

    new Thread(searchModel::executeTaggerMovement).start();
  }

  @Override
  public void show() {
    System.out.println(".()");
  }

  @Override
  public void requestFocus() {
    mazeView.requestFocus();
  }
}
