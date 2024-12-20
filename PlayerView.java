import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class PlayerView extends JPanel {
    private PlayerModel playerModel;

    public PlayerView(PlayerModel playerModel) {
        this.playerModel = playerModel;
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(playerModel.getPlayerX() * MazeModel.MAZE_CELL_SIZE, playerModel.getPlayerY() * MazeModel.MAZE_CELL_SIZE, MazeModel.MAZE_CELL_SIZE, MazeModel.MAZE_CELL_SIZE);
    }
}
