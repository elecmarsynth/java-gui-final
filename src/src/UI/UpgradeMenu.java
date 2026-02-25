package UI;
import java.awt.*;
import javax.swing.*;

import Tower.PlantTower;
import panelCore.GamePanel;

public class UpgradeMenu extends JPanel {
    GamePanel gp;
    TowerMenu towerMenu;
    PlantTower selectedTower;
    private CardLayout cardLayout;
    private JPanel bottomPanel;

    // แก้ไข Constructor ให้รับ CardLayout และ Panel หลักเข้ามา
    public UpgradeMenu(GamePanel gp, CardLayout cl, JPanel bp,TowerMenu towerMenu) {
        this.gp = gp;
        this.cardLayout = cl;
        this.bottomPanel = bp;
        this.towerMenu = towerMenu;
        this.setBackground(Color.DARK_GRAY);
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        updateMenu(null);
    }
    // ย้าย Method นี้มาไว้ที่นี่ตามต้องการ
    public void showUpgrade(PlantTower tower) {
        this.updateMenu(tower); 
        if (cardLayout != null && bottomPanel != null) {
            cardLayout.show(bottomPanel, "UpgradeMenu"); // สั่งแสดงตัวเอง
            bottomPanel.revalidate();
            bottomPanel.repaint();
        }
    }
    public void updateMenu(PlantTower tower) {
    this.selectedTower = tower;
    this.removeAll();

    if (tower != null) {
        // เช็คว่าป้อมนี้รองรับการอัปเกรด (Implement UpgradeTower ไว้) หรือไม่
        if (tower instanceof UpgradeTower) {
            UpgradeTower ut = (UpgradeTower) tower;

            // แสดงชื่อป้อมและเลเวล
            JLabel label = new JLabel(ut.getName() + " Level: " + ut.getLevel());
            label.setForeground(Color.WHITE);
            this.add(label);

            // แสดงปุ่มอัปเกรด พร้อมราคาที่ดึงมาจากแต่ละคลาสเอง
            JButton btnUpgrade = new JButton("Upgrade (" + ut.getUpgradeCost() + ")");
            btnUpgrade.addActionListener(e -> {
                int cost = ut.getUpgradeCost();
                if (gp.getCoins().spendCoins(cost)) { // ✅ ตัดเงินก่อน
                    if (ut.upgrade()) {
                        tower.addSpent(cost); // ✅ บันทึกค่าอัปเกรด
                        updateMenu(tower);
                    } else {
                        gp.getCoins().addCoins(cost); // ✅ คืนเงินถ้าอัปเกรดไม่ได้
                    }
                }
            });
            this.add(btnUpgrade);
        }else {
            // กรณีเป็นป้อมที่อัปเกรดไม่ได้ (ถ้ามี)
            JLabel label = new JLabel("Tower: " + tower.getClass().getSimpleName());
            label.setForeground(Color.WHITE);
            this.add(label);
        }
        int sellPrice = (int)(tower.getTotalSpent() * 0.7);
        JButton btnSell = new JButton("Sell (+" + sellPrice + "💵)");
        btnSell.addActionListener(e -> sellTower(tower));
        this.add(btnSell);

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