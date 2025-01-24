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
  private boolean isHeartbeatPlaying = false;

  public TaggerModel(MazeModel mazeModel) {
    this.mazeModel = mazeModel;
    this.currentDirection = Direction.LEFT;
    this.taggerX = startX;
    this.taggerY = startY;

    this.heartbeatSoundSlow = SoundManager.loadClip("/sounds/heartbeat/heartbeatSlowCut.wav");
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

  public void setIsHeartbeatPlaying(boolean isHeartbeatPlaying) {
    this.isHeartbeatPlaying = isHeartbeatPlaying;
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

  private boolean getPlayerInRangeOfHeartbeat() {
    PlayerModel playerModel = mazeModel.getPlayerModel();

    float playerX = playerModel.getPlayerX();
    float playerY = playerModel.getPlayerY();

    double distanceSquared = Math.pow(taggerX - playerX, 2) + Math.pow(taggerY - playerY, 2);

    if (distanceSquared <= 7.0f * 7.0f) {
      return true;
    } else {
      return false;
    }
  }

  public Direction getCurrentDirection() {
    return currentDirection;
  }

  private void handleHeartbeatSound() {
    if (getPlayerInRangeOfHeartbeat()) {
      if (!isHeartbeatPlaying) {
        System.out.println("play heartbeat sound");
        SoundManager.playClipLoopFadeIn(heartbeatSoundSlow, 3000, -40.0f, 0.0f, mazeModel, this);
        isHeartbeatPlaying = true;
      }
    } else {
      if (isHeartbeatPlaying) {
        System.out.println("stop heartbeat sound");
        SoundManager.stopClipFadeOut(heartbeatSoundSlow, 3000, 0.0f, -40.0f);
        isHeartbeatPlaying = false;
      }
    }
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
              searchModel.signalConditionMet1();
              ((Timer) e.getSource()).stop();
            }
          });
          timer.start();
        }
      }
    }
  }

  private void notifyChange() {
    setChanged();
    notifyObservers();
  }
}
