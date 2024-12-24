package maze.window;

import java.util.*;
import java.util.stream.Collectors;

import maze.window.screen.AppScreen;

public class AppScreenManager extends maze.util.Observable {
  private static AppScreenManager INSTANCE = new AppScreenManager();
  private Stack<AppScreen> stack = new Stack<AppScreen>();

  public List<AppScreen> getScreensAsList() {
    List<AppScreen> list = new LinkedList<>(stack);
    Collections.reverse(list);
    return list;
  }

  public Stack<AppScreen> getStackCopy() {
    return (Stack<AppScreen>) stack.clone();
  }

  public void push(AppScreen window) {
    stack.push(window);
    setChanged();
    notifyObservers();
    System.out.println("pushed");
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

  public AppScreen peek() {
    return stack.peek();
  }

  public static AppScreenManager getInstance() {
    return INSTANCE;
  }
}
