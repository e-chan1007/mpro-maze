package maze.util;

import java.util.function.Consumer;

import javax.swing.Timer;

public class Fader {
  public static final Fader FADE_OUT = new Fader(-1, 1);
  public static final Fader FADE_IN = new Fader(1, 0);

  private final int sign;
  private final float initialValue;
  private float currentValue;

  private Fader(int sign, float initialOpacity) {
    this.sign = sign;
    this.initialValue = initialOpacity;
  }

  private void updateCurrentValue() {
    currentValue += 0.08f * sign;
    if (currentValue < 0)
      currentValue = 0;
    if (currentValue > 1)
      currentValue = 1;
  }

  public Timer createTimer(Consumer<Float> onUpdate, Runnable onFinished) {
    try {
      Fader newFader = (Fader) clone();
      Timer timer = new Timer(1000 / 60, e -> {
        newFader.updateCurrentValue();
        onUpdate.accept(newFader.currentValue);
        if (newFader.currentValue == 0 || newFader.currentValue == 1) {
          ((Timer) e.getSource()).stop();
          if (onFinished != null)
            onFinished.run();
        }
      }) {
        @Override
        public void start() {
          newFader.currentValue = newFader.initialValue;
          super.start();
        }
      };
      return timer;
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return new Fader(sign, initialValue);
  }
}
