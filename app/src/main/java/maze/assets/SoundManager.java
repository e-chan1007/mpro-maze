package maze.assets;

import java.io.BufferedInputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * @example SoundManager.playClip(SoundManager.loadClip("/se.wav"));
 */
public class SoundManager extends Thread {
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
   * 
   * @param clip
   * @param fadeMillis
   * @param startDb
   * @param endDb
   */

  public static void fadeOutAndStop(Clip clip, int fadeMillis, float startDb, float endDb) {
    if (clip == null)
      return;

    // フェードステップの数(細かくすると滑らかになるが時間のオーバーヘッドが増える)
    int steps = 20;
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
      // 最後に無音に近づけて完全にstop
      setVolume(clip, endDb);
      clip.stop();
    }).start();
  }

  public static void fadeInAndLoop(Clip clip, int fadeMillis, float startDb, float endDb) {
    if (clip == null)
      return;

    clip.setFramePosition(0);
    clip.loop(Clip.LOOP_CONTINUOUSLY); // ループ再生開始

    int steps = 20;
    float delta = (endDb - startDb) / steps;
    long sleepDuration = fadeMillis / steps;

    // 最初は無音にしてから再生開始
    setVolume(clip, startDb);

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
      // フェードイン完了後、最終値にしておく
      setVolume(clip, endDb);
    }).start();
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
}
