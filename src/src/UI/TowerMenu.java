package UI;
import java.awt.*;
import javax.swing.*;

import panelCore.GamePanel;

public class TowerMenu extends JPanel {
    GamePanel gp;
    private CardLayout cardLayout;
    private JPanel bottomPanel;
    public TowerMenu(GamePanel gp, CardLayout cl, JPanel bp) {
        this.gp = gp;
        this.cardLayout = cl;
        this.bottomPanel = bp;
        this.setPreferredSize(new Dimension(1280, 100));
        this.setBackground(Color.DARK_GRAY);
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton btnTurret = new JButton("Turret (Gray)");
        btnTurret.addActionListener(e -> gp.setSelectedTower(1));
        this.add(btnTurret);
        JButton btnMiner = new JButton("Miner (Red)");
        btnMiner.addActionListener(e -> gp.setSelectedTower(2));
        this.add(btnMiner);
    }
    public void showTower() {
        if (cardLayout != null && bottomPanel != null) {
            cardLayout.show(bottomPanel, "TowerMenu");
            bottomPanel.revalidate();
            bottomPanel.repaint();
        }
    }
}