package maze.asset;

import java.io.BufferedInputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import maze.maze.MazeModel;
import maze.maze.tagger.TaggerModel;

/**
 * @example SoundManager.playClip(SoundManager.loadClip("/se.wav"));
 */
public class SoundManager {
  /**
   * 音声クリップ(.wav)を読み込む
   *
   * @param path ファイルパス(app/src/main/resources/以下)
   * @return
   */
  public static Clip loadClip(String path) {
    try (AudioInputStream inputStream = AudioSystem
        .getAudioInputStream(new BufferedInputStream(SoundManager.class.getResourceAsStream(path)))) {
      Clip clip = AudioSystem.getClip();
      clip.open(inputStream);
      System.out.println("Loaded: " + path);
      return clip;
    } catch (Exception e) {
      System.out.println("Error: " + path);
      e.printStackTrace();
    }
    return null;
  }

  public static void setVolume(Clip clip, float volume) {
    if (clip == null) {
      return;
    } else {
      try {
        FloatControl volumecontrol = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        volumecontrol.setValue(volume);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 音声クリップを再生する
   */
  public static void playClip(Clip clip) {
    new Thread(() -> {
      clip.setFramePosition(0);
      clip.start();
    }).start();
  }

  public static void stopClip(Clip clip) {
    if (clip == null) {
      return;
    } else {
      clip.stop();
    }
  }

  /*
   * 音声クリップをフェードイン/フェードアウトする
   */
  public static void fade(Clip clip, int fadeMillis, float startDb, float endDb, Runnable onFinish) {
    if (clip == null)
      return;

    int steps = 1000;
    float delta = (endDb - startDb) / steps;
    long sleepDuration = fadeMillis / steps;

    new Thread(() -> {
      for (int i = 0; i < steps; i++) {
        float currentDb = startDb + delta * i;
        setVolume(clip, currentDb);
        try {
          Thread.sleep(sleepDuration);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      setVolume(clip, endDb);
    }).start();
  }

  /*
   * 音声クリップをループ再生する
   */
  public static void playClipLoopFadeIn(Clip clip, int fadeMillis, float startDb, float endDb, MazeModel mazeModel, TaggerModel taggerModel) {
    if (clip == null)
      return;

    setVolume(clip, startDb);

    clip.setFramePosition(0);

    clip.loop(Clip.LOOP_CONTINUOUSLY);

    fade(clip, fadeMillis, startDb, endDb, null);

    new Thread(() -> {
      try {
        while (true) {
          if (mazeModel.isPaused()) {
            taggerModel.setIsHeartbeatPlaying(false);
            clip.stop();
            clip.flush();
          }
          Thread.sleep(100);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();

  }

  /*
   * 音声クリップをフェードアウトして停止する
   */
  public static void stopClipFadeOut(Clip clip, int fadeMillis, float startDb, float endDb) {
    if (clip == null)
      return;

    fade(clip, fadeMillis, startDb, endDb, () -> {
      clip.stop();
      clip.flush();
    });
  }

}
