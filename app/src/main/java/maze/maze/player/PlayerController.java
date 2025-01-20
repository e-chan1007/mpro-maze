package maze.maze.player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayerController implements KeyListener {
  private final PlayerModel playerModel;

  public PlayerController(PlayerModel playerModel) {
    this.playerModel = playerModel;
  }

  @Override
  public void keyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode();
    switch (keyCode) {
      case KeyEvent.VK_LEFT, KeyEvent.VK_A -> playerModel.moveLeft();
      case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> playerModel.moveRight();
      case KeyEvent.VK_UP, KeyEvent.VK_W -> playerModel.moveUp();
      case KeyEvent.VK_DOWN, KeyEvent.VK_S -> playerModel.moveDown();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }
}
