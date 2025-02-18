package maze.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorListener;

import maze.util.Observable;
import maze.util.Observer;

class MenuEntry extends AbstractMap.SimpleEntry<String, Runnable> {
  public MenuEntry(String key, Runnable value) {
    super(key, value);
  }
}

public class Menu extends Observable {
  private MenuEntry active = null;
  private final List<MenuEntry> menuItems = new ArrayList<>();

  public Menu add(String label, Runnable action) {
    menuItems.add(new MenuEntry(label, action));
    return this;
  }

  public MenuComponent build() {
    MenuComponent menuComponent = new MenuComponent();
    return menuComponent;
  }

  private void setActive(MenuEntry entry) {
    active = entry;
    setChanged();
    notifyObservers(entry);
  }

  private void selectNext() {
    int index = menuItems.indexOf(active);
    setActive(menuItems.get((index + 1) % menuItems.size()));
  }

  private void selectPrevious() {
    int index = menuItems.indexOf(active);
    setActive(menuItems.get((index - 1 + menuItems.size()) % menuItems.size()));
  }

  public class MenuComponent extends JPanel {
    private MenuComponent() {
      super();
      setOpaque(true);
      setBackground(new Color(0, 0, 0, 0));
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      setAlignmentX(CENTER_ALIGNMENT);
      setAlignmentY(CENTER_ALIGNMENT);

      menuItems.forEach((entry) -> {
        MenuItemComponent menuItemComponent = new MenuItemComponent(Menu.this, entry);
        add(menuItemComponent);
      });

      setActive(menuItems.get(0));
    }

    private class MenuItemComponent extends JPanel implements Observer {
      private static final Font UI_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
      private final JButton menuButton;
      private final MenuEntry entry;

      private MenuItemComponent(Menu menu, MenuEntry entry) {
        super();
        menu.addObserver(this);
        this.entry = entry;
        setOpaque(true);
        setBackground(new Color(0, 0, 0, 0));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setAlignmentX(LEFT_ALIGNMENT);

        JLabel activeLabel = new JLabel("▶");
        activeLabel.setForeground(null);
        activeLabel.setFont(UI_FONT);
        activeLabel.setOpaque(true);
        activeLabel.setBackground(new Color(0, 0, 0, 0));
        add(activeLabel);

        menuButton = new JButton(entry.getKey());
        menuButton.addActionListener(e -> {
          active.getValue().run();
        });
        menuButton.setOpaque(true);
        menuButton.setBackground(new Color(0, 0, 0, 0));
        menuButton.setBorder(null);
        menuButton.setFocusPainted(false);
        menuButton.setContentAreaFilled(false);
        menuButton.setForeground(Color.WHITE);
        menuButton.setFont(UI_FONT);
        // menuItem.addMouseMotionListener(new MouseMotionAdapter() {
        // @Override
        // public void mouseMoved(java.awt.event.MouseEvent e) {
        // state.setActive(menuItem.getText());
        // }
        // });
        menuButton.addKeyListener(new KeyAdapter() {
          @Override
          public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
              case KeyEvent.VK_UP -> selectPrevious();
              case KeyEvent.VK_DOWN -> selectNext();
              case KeyEvent.VK_ENTER -> menuButton.doClick();
            }
          }
        });
        menuButton.addFocusListener(new FocusAdapter() {
          @Override
          public void focusGained(FocusEvent e) {
            setActive(entry);
          }
        });
        add(menuButton);

      }

      @Override
      public void update(Observable o, Object arg) {
        boolean focused = arg == entry;
        if (focused) {
          SwingUtilities.invokeLater(() -> {
            menuButton.addAncestorListener(new AncestorListener() {
              @Override
              public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                menuButton.requestFocusInWindow();
              }

              @Override
              public void ancestorMoved(javax.swing.event.AncestorEvent event) {
              }

              @Override
              public void ancestorRemoved(javax.swing.event.AncestorEvent event) {
              }
            });
          });
        }
        ((JLabel) getComponent(0)).setForeground(focused ? Color.WHITE : null);
      }
    }
  }
}
