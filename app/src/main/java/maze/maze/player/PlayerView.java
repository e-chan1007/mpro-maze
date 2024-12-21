package maze.maze.player;

import java.awt.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;

import maze.maze.*;

public class PlayerView {
    private PlayerModel playerModel;

    public PlayerView(PlayerModel playerModel) {
        this.playerModel = playerModel;
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        Graphics2D g2d = (Graphics2D) g;
        g2d.fill(new Ellipse2D.Float(playerModel.getPlayerX() * MazeModel.MAZE_CELL_SIZE,
                playerModel.getPlayerY() * MazeModel.MAZE_CELL_SIZE, MazeModel.MAZE_CELL_SIZE,
                MazeModel.MAZE_CELL_SIZE));
    }
}
