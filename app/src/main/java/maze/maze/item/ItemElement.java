package maze.maze.item;

import maze.maze.element.MazeElement;


abstract public class ItemElement extends MazeElement {
    private boolean isItemCollected = false;

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
