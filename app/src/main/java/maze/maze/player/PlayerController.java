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
    if (keyCode == KeyEvent.VK_LEFT) {
      // Move player left
      playerModel.moveLeft();
    } else if(keyCode == KeyEvent.VK_RIGHT) {
      // Move player right
      playerModel.moveRight();
    } else if(keyCode == KeyEvent.VK_UP) {
      // Move player up
      playerModel.moveUp();
    } else if(keyCode == KeyEvent.VK_DOWN) {
      // Move player down
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
