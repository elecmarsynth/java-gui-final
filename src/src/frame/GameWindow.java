package frame;

import Tower.PlantTower;
import UI.ImageLoader;
import UI.TowerMenu;
import UI.UpgradeMenu;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import panelCore.GamePanel;

public class GameWindow {
    private CardLayout cardLayout = new CardLayout();
    private JPanel bottomPanel = new JPanel(cardLayout);
    private TowerMenu towerMenu;
    private UpgradeMenu upgradeMenu;

    public GameWindow() {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        ImageLoader.loadAll();
        GamePanel gamePanel = new GamePanel(this);
        window.add(gamePanel, BorderLayout.CENTER);
        towerMenu = new TowerMenu(gamePanel, cardLayout, bottomPanel);
        upgradeMenu = new UpgradeMenu(gamePanel, cardLayout, bottomPanel, towerMenu);
        bottomPanel.add(towerMenu, "TowerMenu");
        bottomPanel.add(upgradeMenu, "UpgradeMenu");
        window.add(bottomPanel, BorderLayout.SOUTH);
        window.pack();
        // window.setSize(1920, 720);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        System.out.println(window.getWidth() + "x" + window.getHeight());
    }

    public void showUpgrade(PlantTower tower) {
        upgradeMenu.showUpgrade(tower);
    }

    public void showTower() {
        towerMenu.showTower();
    }
}