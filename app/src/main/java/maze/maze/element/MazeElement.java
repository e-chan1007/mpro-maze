package maze.maze.element;

import java.awt.Graphics;

import maze.util.Observable;

/**
 * 迷路の構成要素(抽象クラス: 継承して使う)
 */
abstract public class MazeElement extends Observable {
  /**
   * 歩けるマスかどうか
   */
  public boolean canEnter() {
    return true;
  };

  /**
   * マスに入ったときの処理
   */
  public void onEnter() {
  };

  /**
   * 迷路の要素を描画する
   *
   * @param g    Graphics
   * @param x    マス左上のX座標
   * @param y    マス左上のY座標
   * @param size マスの高さと幅
   */
  abstract public void draw(Graphics g, int x, int y, int size);
}
