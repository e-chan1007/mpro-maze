package maze.util;

import java.util.function.Consumer;

import javax.swing.Timer;

public class Blinker {
  private int sign;
  private boolean isPaused;
  private float currentValue;
  private final float fadeRatePerFrame;
  private final int onDuration;
  private final int offDuration;

  public Blinker(float initialValue, int fadeDuration, int onDuration, int offDuration) {
    this.sign = (fadeDuration < 0.5) ? 1 : -1;
    this.currentValue = initialValue;
    this.fadeRatePerFrame = 1000f / 60 / fadeDuration;
    this.onDuration = onDuration;
    this.offDuration = offDuration;
  }

  private void update() {
    currentValue += sign * fadeRatePerFrame;
    if (currentValue < 0)
      currentValue = 0;
    if (currentValue > 1)
      currentValue = 1;
  }

  public Timer createTimer(Consumer<Float> onUpdate, Runnable onStart) {
    Timer timer = new Timer(1000 / 60, e -> {
      if (isPaused)
        return;
      update();
      onUpdate.accept(currentValue);
      if (currentValue <= 0 || 1 <= currentValue) {
        isPaused = true;
        Timer pauseTimer = new Timer(sign == 1 ? onDuration : offDuration, e2 -> {
          sign *= -1;
          isPaused = false;
        });
        pauseTimer.setRepeats(false);
        pauseTimer.start();
      }
    }) {
      @Override
      public void start() {
        super.start();
        if (onStart != null)
          onStart.run();
      }
    };
    return timer;
  }

  public Timer createTimer(float initialValue, Consumer<Float> onUpdate) {
    return createTimer(onUpdate, () -> {
      currentValue = initialValue;
    });
  }

  public Timer createTimer(Consumer<Float> onUpdate) {
    return createTimer(onUpdate, null);
  }
}
