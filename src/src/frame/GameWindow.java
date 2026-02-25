package frame;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Tower.PlantTower;
import UI.TowerMenu;
import UI.UpgradeMenu;
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
    GamePanel gamePanel = new GamePanel(this);
    window.add(gamePanel, BorderLayout.CENTER);
    towerMenu = new TowerMenu(gamePanel, cardLayout, bottomPanel);
    upgradeMenu = new UpgradeMenu(gamePanel, cardLayout, bottomPanel,towerMenu);
    bottomPanel.add(towerMenu, "TowerMenu");
    bottomPanel.add(upgradeMenu, "UpgradeMenu");
    window.add(bottomPanel, BorderLayout.SOUTH);
    window.pack();                      // ✅ auto คำนวณจาก preferredSize ของทุก component
    window.setLocationRelativeTo(null); // ✅ เปิดตรงกลางจอ
    window.setVisible(true);
    }
    public void showUpgrade(PlantTower tower) {
    upgradeMenu.showUpgrade(tower); // ส่งไม้ต่อให้ upgradeMenu จัดการตัวเอง
    }
    // ใน GameWindow.java (ตัวกลาง)
    public void showTower() {
        towerMenu.showTower(); // เรียกใช้เมธอดที่ย้ายไปอยู่ใน TowerMenu
    }    
}