package maze.window.screen;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLayeredPane;

import maze.maze.InventoryOverlay;
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

  private JLayeredPane layeredPane;
  private InventoryOverlay inventoryOverlay;

  public MazePlayScreen() {
    MazeModel mazeModel = new MazeModel();
    PlayerModel playerModel = new PlayerModel(mazeModel);
    TaggerModel taggerModel = new TaggerModel(mazeModel);
    searchModel = new TaggerSearchModel(mazeModel, playerModel, taggerModel);

    mazeModel.setPlayerModel(playerModel);
    taggerModel.setSearchModel(searchModel);

    mazeView = new MazeView(mazeModel, playerModel, taggerModel);

    PlayerController playerController = new PlayerController(playerModel);

    playerModel.addObserver(mazeView);
    taggerModel.addObserver(mazeView);
    mazeModel.addObserver(mazeView);
    mazeView.addKeyListener(playerController);
    mazeView.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
          case KeyEvent.VK_ESCAPE -> {
            mazeModel.setPaused(true);
            AppScreenManager.getInstance().push(new MazePauseScreen(mazeModel));
          }
        }
      }
    });

    playerModel.addObserver(mazeView);
    taggerModel.addObserver(mazeView);

    layeredPane = new JLayeredPane();
    layeredPane.setPreferredSize(new Dimension(1920, 1080));

    mazeView.setBounds(0, 0, 1920, 1080);
    layeredPane.add(mazeView, JLayeredPane.DEFAULT_LAYER);

    inventoryOverlay = new InventoryOverlay(playerModel);
    playerModel.addObserver(inventoryOverlay);
    inventoryOverlay.setBounds(1920 / 2 - 310, 900, 620, 120);
    layeredPane.add(inventoryOverlay, JLayeredPane.PALETTE_LAYER);

    add(layeredPane);

    new Thread(searchModel::executeTaggerMovement).start();
  }

  @Override
  public void requestFocus() {
    mazeView.requestFocus();
  }

  public void refreshInventoryOverlay() {
    inventoryOverlay.repaint();
  }
}
