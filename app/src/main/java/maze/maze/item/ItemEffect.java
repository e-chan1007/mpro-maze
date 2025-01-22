package maze.maze.item;

import maze.maze.player.PlayerModel;

public interface ItemEffect {
    void applyEffect(PlayerModel playerModel);

    default String getName() {
        return "Unknown Item";
    }
}
