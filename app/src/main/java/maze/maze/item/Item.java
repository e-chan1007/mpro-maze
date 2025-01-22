package maze.maze.item;

import maze.maze.player.PlayerModel;

public interface Item {
    void applyEffect(PlayerModel playerModel);

    String getImagePath();

    String getName();
}
