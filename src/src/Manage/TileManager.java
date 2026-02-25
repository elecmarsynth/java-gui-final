package Manage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage; // 1. นำเข้า library รูปภาพ
import java.io.IOException;      // 1. นำเข้า library อ่านไฟล์
import java.util.ArrayList;        // 1. นำเข้าการจัดการ error
import javax.imageio.ImageIO;

import Tower.GoldMine;
import UI.ImageLoader;
import panelCore.GamePanel;

public class TileManager {
    
    GamePanel gp;
    public int[][] mapData;
    
    // 2. ประกาศตัวแปรเก็บรูปภาพ
    public BufferedImage grassImage; 
    public ArrayList<GoldMine> goldMines = new ArrayList<>();
    public TileManager(GamePanel gp) {
        this.gp = gp;
        mapData = new int[gp.getMaxRow()][gp.getMaxCol()];
        
        // 3. เรียกฟังก์ชันโหลดรูป
        getTileImage();
        
        setupMap();
    }

    // 4. สร้างฟังก์ชันสำหรับโหลดรูปภาพ (แยกออกมาจะได้เป็นระเบียบ)
    public void getTileImage() {
        try {
            // โหลดรูปจากโฟลเดอร์ image (เครื่องหมาย / หมายถึงเริ่มจาก src)
            grassImage = ImageIO.read(getClass().getResourceAsStream("/image/a.png"));
            
            // ถ้ามีรูปอื่นก็โหลดต่อตรงนี้ได้เลย เช่น
            // wallImage = ImageIO.read(getClass().getResourceAsStream("/image/wall.png"));

        } catch (IOException e) {
            e.printStackTrace(); // ถ้าหาไฟล์ไม่เจอ ให้แจ้งเตือนสีแดงใน Console
        }
    }

    public void setupMap() {
        // ... (โค้ด setupMap เดิม ไม่ต้องแก้) ...
        for (int r = 0; r < gp.getMaxRow(); r++) {
            for (int c = 0; c < gp.getMaxCol(); c++) {
                 // ... code เดิม ...
                 if (r == 0 || r == gp.getMaxRow() - 1 || c == 0 || c == gp.getMaxCol() - 1) mapData[r][c] = 9; 
                 else if (r == gp.getMaxRow() / 2 && c == gp.getMaxCol() / 2) mapData[r][c] = 1; 
                 else mapData[r][c] = 0; 
            }
        }
        placeMines();
    }
    
    // ... (placeMines เดิม ไม่ต้องแก้) ...
    // แก้ placeMines() ให้สร้าง GoldMine object ด้วย
    private void placeMines() {
        int minesToPlace = 3;
        while (minesToPlace > 0) {
            int r = (int)(Math.random() * (gp.getMaxRow() - 2)) + 1;
            int c = (int)(Math.random() * (gp.getMaxCol() - 2)) + 1;
            if (mapData[r][c] == 0 && (Math.abs(r - gp.getMaxRow()/2) + Math.abs(c - gp.getMaxCol()/2) > 2)) {
                mapData[r][c] = 2;
                goldMines.add(new GoldMine(c, r)); // ✅ เพิ่มบรรทัดนี้
                minesToPlace--;
        }
    }
}
    public ArrayList<GoldMine> getGoldMines() {
    return goldMines;
}

    // 5. แก้ไขส่วนการวาด (Draw)
    public void draw(Graphics2D g2) {
    for (int r = 0; r < gp.getMaxRow(); r++) {
        for (int c = 0; c < gp.getMaxCol(); c++) {
            int tileType = mapData[r][c];
            int x = c * gp.getTileSize();
            int y = r * gp.getTileSize();

            // ✅ วาดหญ้าทุกช่องเสมอ (รวมถึงช่องที่มีป้อม)
            if (grassImage != null) {
                g2.drawImage(grassImage, x, y, gp.getTileSize(), gp.getTileSize(), null);
            } else {
                g2.setColor(new Color(34, 139, 34));
                g2.fillRect(x, y, gp.getTileSize(), gp.getTileSize());
            }

            // ✅ วาด overlay ทับหญ้า เฉพาะช่องพิเศษ
            if (tileType == 9) {
                g2.setColor(new Color(60, 60, 60, 180)); // กำแพง (โปร่งแสงนิดหน่อย)
                g2.fillRect(x, y, gp.getTileSize(), gp.getTileSize());
            } else if (tileType == 2) {
                // เหมืองทอง — วาดรูปทับหญ้า
                if (ImageLoader.goldMine != null) {
                    g2.drawImage(ImageLoader.goldMine, x, y, gp.getTileSize(), gp.getTileSize(), null);
                } else {
                    g2.setColor(Color.ORANGE);
                    g2.fillOval(x + 5, y + 5, gp.getTileSize() - 10, gp.getTileSize() - 10);
                }
            }
            // ✅ type 1 (Base) และ type 3 (Tower) ปล่อยให้ TowerManager.draw() วาดเองทีหลัง

            // เส้นตาราง
            g2.setColor(new Color(0, 0, 0, 40));
            g2.drawRect(x, y, gp.getTileSize(), gp.getTileSize());
        }
    }
}
}