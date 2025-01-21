package maze.assets;

import java.io.BufferedInputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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
