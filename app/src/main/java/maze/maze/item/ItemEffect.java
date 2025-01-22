package maze.maze.item;

import maze.maze.player.PlayerModel;

public interface ItemEffect {
    void applyEffect(PlayerModel playerModel);

    default String getImagePath() {
        if (this instanceof HealEffect) {
            return "/item/heal.png";
        } else {
            return "/item/heal.png";
        }
    }

    default String getName() {
        return "Unknown Item";
    }
}
