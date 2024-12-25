package maze.maze.player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import maze.maze.MazeView;

public class PlayerView {
    private PlayerModel playerModel;
    private MazeView mazeView;

    public PlayerView(PlayerModel playerModel, MazeView mazeView) {
        this.playerModel = playerModel;
        this.mazeView = mazeView;
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        Graphics2D g2d = (Graphics2D) g;
        g2d.fill(new Ellipse2D.Float(playerModel.getPlayerX() * mazeView.getMazeCellSize(),
                playerModel.getPlayerY() * mazeView.getMazeCellSize(), mazeView.getMazeCellSize(),
                mazeView.getMazeCellSize()));
    }
}
