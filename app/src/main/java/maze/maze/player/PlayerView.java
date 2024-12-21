package maze.maze.player;

import java.awt.Color;
import java.awt.Graphics;
import maze.maze.*;

public class PlayerView {
    private PlayerModel playerModel;

    public PlayerView(PlayerModel playerModel) {
        this.playerModel = playerModel;
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(
                playerModel.getPlayerX() * MazeModel.MAZE_CELL_SIZE + MazeModel.MAZE_CELL_SIZE / 4,
                playerModel.getPlayerY() * MazeModel.MAZE_CELL_SIZE + MazeModel.MAZE_CELL_SIZE / 4,
                MazeModel.MAZE_CELL_SIZE * 2 / 4,
                MazeModel.MAZE_CELL_SIZE * 2 / 4);
    }
}
