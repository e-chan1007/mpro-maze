package maze.maze.tagger;

import javax.sound.sampled.Clip;
import javax.swing.Timer;

import maze.asset.SoundManager;
import maze.maze.MazeModel;
import maze.maze.player.PlayerModel;
import maze.util.Observable;

public class TaggerModel extends Observable {
  private float startX = 1;
  private float startY = 1;
  private float taggerX;
  private float taggerY;
  private final int STEPS = 30;
  private final int DELAY = 1000 / 60;
  private boolean canMove = true;
  private Direction currentDirection;
  private MazeModel mazeModel;
  private TaggerSearchModel searchModel;

  private Clip heartbeatSoundSlow;
  private Clip heartbeatSoundMedium;
  private Clip heartbeatSoundFast;
  private int currentStage = 0;
  private boolean isHeartbeatPlaying = false;

  public TaggerModel(MazeModel mazeModel) {
    this.mazeModel = mazeModel;
    this.currentDirection = Direction.LEFT;
    this.taggerX = startX;
    this.taggerY = startY;

    this.heartbeatSoundSlow = SoundManager.loadClip("/sounds/heartbeat/HeartbeatSlow.wav");
    this.heartbeatSoundMedium = SoundManager.loadClip("/sounds/heartbeat/HeartbeatMedium.wav");
    this.heartbeatSoundFast = SoundManager.loadClip("/sounds/heartbeat/HeartbeatFast.wav");
  }

  public MazeModel getMazeModel() {
    return mazeModel;
  }

  public void setSearchModel(TaggerSearchModel searchModel) {
    this.searchModel = searchModel;
  }

  public float getTaggerX() {
    return taggerX;
  }

  public float getTaggerY() {
    return taggerY;
  }

  public boolean getCanMoveFlag() {
    return canMove;
  }

  public boolean taggerArrivedFlag = false;

  public boolean getTaggerArrived() {
    return taggerArrivedFlag;
  }

  public void setStartPosition(float x, float y) {
    this.startX = x;
    this.startY = y;
    this.taggerX = x;
    this.taggerY = y;
    notifyChange();
  }

  public enum Direction {
    LEFT, RIGHT, UP, DOWN
  }

  public Direction getCurrentDirection() {
    return currentDirection;
  }

  public void moveLeft() {
    currentDirection = Direction.LEFT;
    move(-1.0f, 0.0f, currentDirection);
  }

  public void moveRight() {
    currentDirection = Direction.RIGHT;
    move(1.0f, 0.0f, currentDirection);
  }

  // * currentDirectionは更新しない */
  public void moveUp() {
    move(0.0f, -1.0f, currentDirection);
  }

  // * currentDirectionは更新しない */
  public void moveDown() {
    move(0.0f, 1.0f, currentDirection);
  }

  public boolean isTaggerInRange() {
    PlayerModel playerModel = mazeModel.getPlayerModel();
    if (playerModel == null) {
      System.out.println("PlayerModelが設定されていません");
      return false;
    }

    float playerX = playerModel.getPlayerX();
    float playerY = playerModel.getPlayerY();

    double distanceSquared = Math.pow(taggerX - playerX, 2) + Math.pow(taggerY - playerY, 2);

    double RANGE = 5.0;
    double RANGE_SQUARED = RANGE * RANGE;

    return distanceSquared <= RANGE_SQUARED;
  }

  private void notifyChange() {
    setChanged();
    notifyObservers();
  }

  private int getCurrentStage() {
    PlayerModel playerModel = mazeModel.getPlayerModel();
    if (playerModel == null) {
      System.out.println("PlayerModelが設定されていません");
      return 0;
    }

    float playerX = playerModel.getPlayerX();
    float playerY = playerModel.getPlayerY();

    double distanceSquared = Math.pow(taggerX - playerX, 2) + Math.pow(taggerY - playerY, 2);

    if (distanceSquared <= 0 * 0) {
      return 3;
    } else if (distanceSquared <= 7.0 * 7.0) {
      return 2;
    } else if (distanceSquared <= 15.0 * 5.0) {
      return 1;
    } else {
      return 0;
    }
  }

  private void handleHeartbeatSound() {
    int newStage = getCurrentStage();
    if (newStage == currentStage) {
      // ステージが同じなら、何もしない(継続)
      return;
    }

    // 以前のStageのクリップをフェードアウト
    // (isHeartbeatPlaying が true の場合に限る)
    if (isHeartbeatPlaying) {
      switch (currentStage) {
        case 3:
          SoundManager.fadeOutAndStop(heartbeatSoundFast, 500, 0.0f, -80.0f);
          break;
        case 2:
          SoundManager.fadeOutAndStop(heartbeatSoundMedium, 500, 0.0f, -80.0f);
          break;
        case 1:
          SoundManager.fadeOutAndStop(heartbeatSoundSlow, 500, 0.0f, -80.0f);
          break;
        default:
          // do nothing
      }
    }

    // 新しいステージに応じたクリップをフェードイン
    switch (newStage) {
      case 3:
        SoundManager.fadeInAndLoop(heartbeatSoundFast, 500, -80.0f, 0.0f);
        isHeartbeatPlaying = true;
        break;
      case 2:
        SoundManager.fadeInAndLoop(heartbeatSoundMedium, 500, -80.0f, 0.0f);
        isHeartbeatPlaying = true;
        break;
      case 1:
        SoundManager.fadeInAndLoop(heartbeatSoundSlow, 500, -80.0f, 0.0f);
        isHeartbeatPlaying = true;
        break;
      default:
        // Stage 0: 音は鳴らさない
        // フェードアウトした後は再生しないので何もしない
        isHeartbeatPlaying = false;
        break;
    }

    currentStage = newStage;
  }

  private void move(float deltaX, float deltaY, Direction direction) {
    int targetX = Math.round(taggerX + deltaX);
    int targetY = Math.round(taggerY + deltaY);
    if (mazeModel.isInMaze(targetX, targetY)) {
      if (mazeModel.getElementAt(targetX, targetY).canEnter()) {
        if (canMove) {
          currentDirection = direction;
          final int[] currentStep = { 0 };
          canMove = false;
          Timer timer = new Timer(DELAY, e -> {
            if (mazeModel.isPaused()) {
              return;
            }
            if (currentStep[0] < STEPS) {
              taggerX += deltaX / STEPS;
              taggerY += deltaY / STEPS;
              notifyChange();
              currentStep[0]++;
            } else {
              canMove = true;
              searchModel.signalConditionMet1();
              ((Timer) e.getSource()).stop();
              handleHeartbeatSound();
            }
          });
          timer.start();
        }
      }
    }
  }
}
