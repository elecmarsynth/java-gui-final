package UI;
import Tower.PlantTower;
import java.awt.*;
import javax.swing.*;
import panelCore.GamePanel;

public class UpgradeMenu extends JPanel {
    GamePanel gp;
    TowerMenu towerMenu;
    PlantTower selectedTower;
    private CardLayout cardLayout;
    private JPanel bottomPanel;
    public UpgradeMenu(GamePanel gp, CardLayout cl, JPanel bp,TowerMenu towerMenu) {
        this.gp = gp;
        this.cardLayout = cl;
        this.bottomPanel = bp;
        this.towerMenu = towerMenu;
        this.setBackground(Color.DARK_GRAY);
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        updateMenu(null);
    }
    public void showUpgrade(PlantTower tower) {
        this.updateMenu(tower); 
        if (cardLayout != null && bottomPanel != null) {
            cardLayout.show(bottomPanel, "UpgradeMenu"); 
            bottomPanel.revalidate();
            bottomPanel.repaint();
        }
    }
    public void updateMenu(PlantTower tower) {
    this.selectedTower = tower;
    this.removeAll();
    if (tower != null) {
        if (tower instanceof UpgradeTower) {
            UpgradeTower ut = (UpgradeTower) tower;
            JLabel label = new JLabel(ut.getName() + " Level: " + ut.getLevel());
            label.setForeground(Color.WHITE);
            this.add(label);
            JButton btnUpgrade = new JButton("Upgrade (" + ut.getUpgradeCost() + ")");
            btnUpgrade.addActionListener(e -> {
                int cost = ut.getUpgradeCost();
                if (gp.getCoins().spendCoins(cost)) { 
                    if (ut.upgrade()) {
                        tower.addSpent(cost); 
                        updateMenu(tower);
                    } else {
                        gp.getCoins().addCoins(cost); 
                    }
                }
            });
            this.add(btnUpgrade);
        }else {
            JLabel label = new JLabel("Tower: " + tower.getClass().getSimpleName());
            label.setForeground(Color.WHITE);
            this.add(label);
        }
        if (!(tower instanceof Tower.BaseTower)) {
            int sellPrice = (int)(tower.getTotalSpent() * 0.7);
            JButton btnSell = new JButton("Sell (+" + sellPrice + "💵)");
            btnSell.addActionListener(e -> sellTower(tower));
            this.add(btnSell);
        }

        JButton btnClose = new JButton("Back");
        btnClose.addActionListener(e -> towerMenu.showTower());
        this.add(btnClose);
    }
        this.revalidate();
        this.repaint();
    }

    private void sellTower(PlantTower tower) {
        int sellPrice = (int)(tower.getTotalSpent() * 0.7);
        gp.getCoins().addCoins(sellPrice);
        gp.getTileM().mapData[tower.getRow()][tower.getCol()] = 0;
        gp.getTowerM().sellTower(tower);
        towerMenu.showTower();
    }
}