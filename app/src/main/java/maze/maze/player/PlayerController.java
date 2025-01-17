package maze.maze.player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayerController implements KeyListener {
  private PlayerModel playerModel;

  public PlayerController(PlayerModel playerModel) {
    this.playerModel = playerModel;
  }

  @Override
  public void keyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode();
    if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
      playerModel.moveLeft();
    } else if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
      playerModel.moveRight();
    } else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
      playerModel.moveUp();
    } else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
      playerModel.moveDown();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }
}
