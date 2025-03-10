package maze.maze.tagger;

import javax.sound.sampled.Clip;
import javax.swing.Timer;

import maze.asset.SoundManager;
import maze.maze.MazeModel;
import maze.maze.element.TaggerStartModel;
import maze.maze.player.PlayerModel;
import maze.util.Observable;
import maze.enums.Direction;

public class TaggerModel extends Observable {
  // 鬼の現在位置
  private float taggerX;
  private float taggerY;

  // 移動の定数
  private static final int STEPS = 30;
  private static final int DELAY = 1000 / 60;

  // 移動可能かどうかのフラグ
  private boolean canMove = true;

  // 鬼の現在の向き
  private Direction currentDirection;

  private MazeModel mazeModel;
  private TaggerSearchModel searchModel;

  // 心音クリップ
  private Clip hearbeatClip;
  private boolean isHeartbeatPlaying = false;

  // 鬼がプレイヤーを追いかけ始める範囲
  private static final double TAGGER_RANGE = 7.0;
  private static final double TAGGER_RANGE_SQUARED = TAGGER_RANGE * TAGGER_RANGE;

  // 心音が鳴る範囲
  private static final double HEARTBEAT_RANGE = 12.0;
  private static final double HEARTBEAT_RANGE_SQUARED = HEARTBEAT_RANGE * HEARTBEAT_RANGE;

  // 鬼がプレイヤーに追いついたかどうかのフラグ
  private boolean taggerArrivedFlag = false;

  // 鬼がプレイヤーと衝突したかどうかのフラグ
  public boolean isTaggerArrived() {
    return taggerArrivedFlag;
  }

  // 鬼がプレイヤーと衝突したかどうかのフラグをセット
  public void setTaggerArrivedFlag(boolean flag) {
    taggerArrivedFlag = flag;
  }

  // コンストラクタ
  public TaggerModel(MazeModel mazeModel) {
    this.mazeModel = mazeModel;
    this.currentDirection = Direction.LEFT;
    this.hearbeatClip = SoundManager.loadClip("/sounds/heartbeat/heartbeat.wav");

    this.mazeModel.addObserver((Observable observable, Object object) -> {
      setStartPos();
    });
  }

  // 鬼の初期位置を設定
  public void setStartPos() {
    int startPos[] = mazeModel.locateElement(TaggerStartModel.class);
    if (startPos != null) {
      taggerX = startPos[0];
      taggerY = startPos[1];
    }
  }

  // MazeModelを返す
  public MazeModel getMazeModel() {
    return mazeModel;
  }

  // TaggerSearchModelをセット
  public void setSearchModel(TaggerSearchModel searchModel) {
    this.searchModel = searchModel;
  }

  // 鬼のX座標を返す
  public float getTaggerX() {
    return taggerX;
  }

  // 鬼のY座標を返す
  public float getTaggerY() {
    return taggerY;
  }

  // 鬼の移動可能フラグを返す
  public boolean getCanMoveFlag() {
    return canMove;
  }

  // 心音再生のフラッグをセット
  public void setIsHeartbeatPlaying(boolean isHeartbeatPlaying) {
    this.isHeartbeatPlaying = isHeartbeatPlaying;
  }

  // 左に移動
  public void moveLeft() {
    currentDirection = Direction.LEFT;
    move(-1.0f, 0.0f, currentDirection);
  }

  // 右に移動
  public void moveRight() {
    currentDirection = Direction.RIGHT;
    move(1.0f, 0.0f, currentDirection);
  }

  // 上に移動 *currentDirectionは更新しない
  public void moveUp() {
    move(0.0f, -1.0f, currentDirection);
  }

  // 下に移動 *currentDirectionは更新しない
  public void moveDown() {
    move(0.0f, 1.0f, currentDirection);
  }

  // プレイヤーが鬼の追跡範囲にいるかどうかを返す
  public boolean isTaggerInRange() {
    PlayerModel playerModel = mazeModel.getPlayerModel();
    if (playerModel == null) {
      System.out.println("PlayerModelが設定されていません");
      return false;
    }
    float playerX = playerModel.getPlayerX();
    float playerY = playerModel.getPlayerY();
    double distanceSquared = Math.pow(taggerX - playerX, 2) + Math.pow(taggerY - playerY, 2);
    return distanceSquared <= TAGGER_RANGE_SQUARED;
  }

  // 心音が鳴る範囲にプレイヤーがいるかどうかを返す
  private boolean isPlayerInRangeOfHeartbeat() {
    PlayerModel playerModel = mazeModel.getPlayerModel();
    float playerX = playerModel.getPlayerX();
    float playerY = playerModel.getPlayerY();
    double distanceSquared = Math.pow(taggerX - playerX, 2) + Math.pow(taggerY - playerY, 2);
    return distanceSquared <= HEARTBEAT_RANGE_SQUARED;
  }

  // 鬼が向いている方向を返す
  public Direction getCurrentDirection() {
    return currentDirection;
  }

  // 心音再生処理
  private void handleHeartbeatSound() {
    if (isPlayerInRangeOfHeartbeat()) {
      if (!isHeartbeatPlaying) {
        System.out.println("play heartbeat sound");
        SoundManager.playClipLoopFadeIn(hearbeatClip, 1000, -40.0f, 0.0f, mazeModel, this);
        isHeartbeatPlaying = true;
      }
    } else {
      if (isHeartbeatPlaying) {
        System.out.println("stop heartbeat sound");
        SoundManager.stopClipFadeOut(hearbeatClip, 1000, 0.0f, -40.0f);
        isHeartbeatPlaying = false;
      }
    }
  }

  // 鬼の移動処理
  private void move(float deltaX, float deltaY, Direction direction) {
    int targetX = Math.round(taggerX + deltaX);
    int targetY = Math.round(taggerY + deltaY);
    if (mazeModel.getElementAt(targetX, targetY).canEnter() && canMove) {
      currentDirection = direction;
      final int[] currentStep = { 0 };
      canMove = false;
      handleHeartbeatSound();
      Timer timer = new Timer(DELAY, e -> {
        if (mazeModel.isPaused()) {
          return;
        }
        if (currentStep[0] < STEPS) {
          taggerX += deltaX / STEPS;
          taggerY += deltaY / STEPS;
          if (!taggerArrivedFlag) {
            taggerArrivedFlag = searchModel.isTaggerAtPlayer();
          }
          notifyChange();
          currentStep[0]++;
        } else {
          canMove = true;
          searchModel.signalConditionMet();
          ((Timer) e.getSource()).stop();
        }
      });
      timer.start();
    }
  }

  // Observerに通知
  private void notifyChange() {
    setChanged();
    notifyObservers();
  }
}
