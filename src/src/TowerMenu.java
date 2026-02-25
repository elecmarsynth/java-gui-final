import java.awt.*;
import javax.swing.*;

public class TowerMenu extends JPanel {
    GamePanel gp;
    private CardLayout cardLayout;
    private JPanel bottomPanel;

    // แก้ไข Constructor ให้รับเครื่องมือจัดการหน้าจอ
    public TowerMenu(GamePanel gp, CardLayout cl, JPanel bp) {
        this.gp = gp;
        this.cardLayout = cl;
        this.bottomPanel = bp;
        
        this.setPreferredSize(new Dimension(1280, 100));
        this.setBackground(Color.DARK_GRAY);
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // ปุ่มเลือก Turret
        JButton btnTurret = new JButton("Turret (Gray)");
        btnTurret.addActionListener(e -> gp.setSelectedTower(1));
        this.add(btnTurret);

        // ปุ่มเลือก Miner
        JButton btnMiner = new JButton("Miner (Red)");
        btnMiner.addActionListener(e -> gp.setSelectedTower(2));
        this.add(btnMiner);
    }

    // ย้าย Method showTower มาไว้ที่นี่
    public void showTower() {
        if (cardLayout != null && bottomPanel != null) {
            cardLayout.show(bottomPanel, "TowerMenu");
            bottomPanel.revalidate();
            bottomPanel.repaint();
        }
    }
}