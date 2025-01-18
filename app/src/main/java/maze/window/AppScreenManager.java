package maze.window;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import maze.window.screen.ScreenBase;

public class AppScreenManager extends maze.util.Observable {
  private static final AppScreenManager INSTANCE = new AppScreenManager();
  private final Stack<ScreenBase> stack = new Stack<>();

  public List<ScreenBase> getScreensAsList() {
    List<ScreenBase> list = new LinkedList<>(stack);
    Collections.reverse(list);
    return list;
  }

  public Stack<ScreenBase> getStackCopy() {
    return (Stack<ScreenBase>) stack.clone();
  }

  public void push(ScreenBase window) {
    stack.push(window);
    setChanged();
    notifyObservers();
  }

  public void pop() {
    stack.pop();
    setChanged();
    notifyObservers();
  }

  public void clear() {
    stack.clear();
    setChanged();
    notifyObservers();
  }

  public ScreenBase peek() {
    return stack.peek();
  }

  public static AppScreenManager getInstance() {
    return INSTANCE;
  }
}
