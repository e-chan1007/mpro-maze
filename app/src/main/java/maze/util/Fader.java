package maze.util;

import java.util.function.Consumer;

import javax.swing.Timer;

public class Fader {
  public static final Fader FADE_OUT = new Fader(-1, 1);
  public static final Fader FADE_IN = new Fader(1, 0);

  private final int sign;
  private final float initialOpacity;
  private float opacity;

  private Fader(int sign, float initialOpacity) {
    this.sign = sign;
    this.initialOpacity = initialOpacity;
  }

  public int getSign() {
    return sign;
  }

  public float getInitialOpacity() {
    return initialOpacity;
  }

  private void updateOpacity() {
    opacity += 0.08f * sign;
    if (opacity < 0)
      opacity = 0;
    if (opacity > 1)
      opacity = 1;
  }

  public Timer createTimer(Consumer<Float> onUpdate, Runnable onFinished) {
    try {
      Fader newFader = (Fader) clone();
      Timer timer = new Timer(1000 / 60, e -> {
        newFader.updateOpacity();
        onUpdate.accept(newFader.opacity);
        if (newFader.opacity == 0 || newFader.opacity == 1) {
          ((Timer) e.getSource()).stop();
          if (onFinished != null)
            onFinished.run();
        }
      }) {
        @Override
        public void start() {
          newFader.opacity = newFader.initialOpacity;
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
    return new Fader(sign, initialOpacity);
  }
}
