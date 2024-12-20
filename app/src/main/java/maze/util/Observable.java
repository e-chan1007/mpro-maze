package maze.util;

import java.util.ArrayList;

public class Observable {

  private ArrayList<Observer> observers = new ArrayList<Observer>();
  private Boolean changed = false;

  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  public void deleteObserver(Observer observer) {
    observers.remove(observer);
  }

  public void setChanged() {
    changed = true;
  }

  public void notifyObservers() {
    if (changed) {
      for (Observer observer : observers) {
        observer.update(this, null);
      }
      changed = false;
    }
  }

  public void notifyObservers(Object arg) {
    if (changed) {
      for (Observer observer : observers) {
        observer.update(this, arg);
      }
      changed = false;
    }
  }

}
