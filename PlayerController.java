import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class PlayerController implements KeyListener {
  @Override
  public void keyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode();
    int playerX = model.getPlayerX();
    if (keyCode == KeyEvent.VK_LEFT) {
      // Move player lef
      
    } else if(keyCode == KeyEvent.VK_RIGHT) {
      // Move player right
    } else if(keyCode == KeyEvent.VK_UP) {
      // Move player up
    } else if(keyCode == KeyEvent.VK_DOWN) {
      // Move player down
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }
}
