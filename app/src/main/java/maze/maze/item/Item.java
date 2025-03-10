package maze.maze.item;

import maze.maze.player.PlayerModel;

public interface Item {
    // アイテム効果を適用するメソッド
    void applyEffect(PlayerModel playerModel);
    // アイテム名を返すメソッド
    String getName();
}