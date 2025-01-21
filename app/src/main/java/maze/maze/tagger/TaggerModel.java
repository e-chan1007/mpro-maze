package maze.maze.tagger;

import javax.swing.Timer;
import javax.sound.sampled.Clip;

import maze.assets.SoundManager;
import maze.maze.MazeModel;
import maze.maze.player.PlayerModel;
import maze.util.Observable;

public class TaggerModel extends Observable {
  private float taggerX = 1;
  private float taggerY = 1;
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

  public boolean getFlag() {
    return canMove;
  }

  public enum Direction {
    LEFT, RIGHT, UP, DOWN
  }

  public Direction getCurrentDirection() {
    return currentDirection;
  }

  public void moveLeft() {
    move(-1.0f, 0.0f, Direction.LEFT);
  }

  public void moveRight() {
    move(1.0f, 0.0f, Direction.RIGHT);
  }

  public void moveUp() {
    move(0.0f, -1.0f, Direction.UP);
  }

  public void moveDown() {
    move(0.0f, 1.0f, Direction.DOWN);
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

    if (distanceSquared <= 3.0 * 3.0) {
      return 3;
    } else if (distanceSquared <= 5.0 * 5.0) {
      return 2;
    } else if (distanceSquared <= 7.0 * 7.0) {
      return 1;
    } else {
      return 0;
    }
  }

  private void handleHeartbeatSound() {
    int newStage = getCurrentStage();

    if (newStage != currentStage) {
      stopHeartbeatSound();

      switch (newStage) {
        case 3:
          SoundManager.playClip(heartbeatSoundFast);
          isHeartbeatPlaying = true;
          break;
        case 2:
          SoundManager.playClip(heartbeatSoundMedium);
          isHeartbeatPlaying = true;
          break;
        case 1:
          SoundManager.playClip(heartbeatSoundSlow);
          isHeartbeatPlaying = true;
          break;
        default:
          isHeartbeatPlaying = false;
          break;
      }
      currentStage = newStage;
    }
  }

  private void stopHeartbeatSound() {
    if (isHeartbeatPlaying) {
      SoundManager.stopClip(heartbeatSoundSlow);
      SoundManager.stopClip(heartbeatSoundMedium);
      SoundManager.stopClip(heartbeatSoundFast);
    }
    isHeartbeatPlaying = false;
  }

  private void move(float deltaX, float deltaY, Direction direction) {
    int targetX = Math.round(taggerX + deltaX);
    int targetY = Math.round(taggerY + deltaY);
    if(mazeModel.isInMaze(targetX, targetY)) {
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
