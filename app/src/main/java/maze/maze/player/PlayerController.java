package maze.maze.player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayerController implements KeyListener {
  // アイテムのキー定義
  private static final int ITEM_1_KEY = KeyEvent.VK_Q;
  private static final int ITEM_2_KEY = KeyEvent.VK_E;
  private static final int ITEM_3_KEY = KeyEvent.VK_R;

  private final PlayerModel playerModel;

  // コンストラクタ
  public PlayerController(PlayerModel playerModel) {
    this.playerModel = playerModel;
  }

  // キー入力時の処理
  @Override
  public void keyPressed(KeyEvent e) {
    if (playerModel.isPaused()) {
      return;
    }
    int keyCode = e.getKeyCode();
    switch (keyCode) {
      case KeyEvent.VK_LEFT, KeyEvent.VK_A -> playerModel.moveLeft();
      case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> playerModel.moveRight();
      case KeyEvent.VK_UP, KeyEvent.VK_W -> playerModel.moveUp();
      case KeyEvent.VK_DOWN, KeyEvent.VK_S -> playerModel.moveDown();
      case ITEM_1_KEY -> playerModel.useItem(0);
      case ITEM_2_KEY -> playerModel.useItem(1);
      case ITEM_3_KEY -> playerModel.useItem(2);
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }
}
