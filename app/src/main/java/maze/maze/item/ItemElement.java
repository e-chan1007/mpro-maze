package maze.maze.item;

import java.awt.Color;
import java.awt.Graphics;

import maze.maze.element.MazeElement;
import maze.maze.player.PlayerModel;


abstract public class ItemElement extends MazeElement {
    private boolean isItemCollected = false;
    private ItemType type;

    public ItemElement(ItemType type) {
        this.type = type;
    }

    public ItemType getType() {
        return type;
    }

    public void collect(PlayerModel player) {
        if (!isItemCollected) {
            isItemCollected = true;
            switch (type) {
                case KEY:
                    player.addKey();
                    break;
                case POTION:
                    player.heal(1);
                    break;
                case TREASURE:
                    player.addScore(100);
                    break;
            }
            setChanged();
            notifyObservers();
        }
    }

    /**
     * アイテムが取得されているかどうか
     */
    public boolean isCollected() {
        return this.isItemCollected;
    }

    /**
     * アイテムが取得されているかどうかを設定する
     *
     * @param isCollected アイテムの取得状態
     */
    public void setCollected(boolean isCollected) {
        this.isItemCollected = isCollected;
        this.setChanged();
        this.notifyObservers();
    }

}
