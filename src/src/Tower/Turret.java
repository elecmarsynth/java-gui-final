package Tower;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import Enemy.Enemy;
import UI.ImageLoader;
import UI.UpgradeTower;

public class Turret extends PlantTower implements UpgradeTower {
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private int cooldown = 0;
    private int level = 1;
    private final int MAX_LEVEL = 3;
    private int upgradeCost = 50; // ราคาเริ่มต้นในการอัปไปเลเวล 2

    public Turret(int col, int row) {
        super(col, row);
        this.maxHp = 200;
        
    }
    public void update(ArrayList<Enemy> enemies) {
        // Update bullets
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).update();
            if (!bullets.get(i).isActive) {
                bullets.remove(i);
                i--;
            }
        }
        // Shooting Logic
        if (cooldown > 0) { cooldown--; return; }

        for (Enemy e : enemies) {
            double dist = Math.hypot(e.getX() - (col*64+32), e.getY() - (row*64+32));
            if (dist < 200) { // Range 200
                bullets.add(new Bullet(col * 64 + 32, row * 64 + 32, e));
                cooldown = 30; 
                break; // ยิงทีละตัว
            }
        }
    }
    public String getName() { 
        return "Turret"; }
    @Override
    public int getLevel() { 
        return level; 
    }
    @Override
    public boolean upgrade() {
        if (isMaxLevel()){ 
            return false; // ถ้าตันแล้ว อัปเกรดไม่ได้
        }
        level++;
        // กำหนดสถานะตามเลเวลที่เปลี่ยนไป (แต่ละคลาสออกแบบได้อิสระ)
        if (level == 2) {
            this.maxHp += 100;       // ถึกขึ้น
            this.upgradeCost = 150;  // ราคาสำหรับอัปไปเลเวล 3
            // เพิ่มดาเมจได้ที่นี่ถ้ามีตัวแปรดาเมจ
        } else if (level == 3) {
            this.maxHp += 200;
            this.upgradeCost = 0;    // เลเวลตันแล้ว
        }
        
        this.hp = this.maxHp; // อัปเกรดเสร็จให้เลือดเต็ม
        return true;
    }
    @Override
    public int getUpgradeCost() { 
        return upgradeCost; 
    }
    @Override
    public boolean isMaxLevel() { 
        return level >= MAX_LEVEL; 
    }
    @Override
    public void draw(Graphics2D g2, int tileSize) {
        int x = col * tileSize;
        int y = row * tileSize;
         if (ImageLoader.turret != null) {
        g2.drawImage(ImageLoader.turret, x, y, tileSize, tileSize, null);
    } else {
        g2.setColor(Color.GRAY);
        g2.fillRect(x + 5, y + 5, tileSize, tileSize);
    }
        
        for (Bullet b : bullets) b.draw(g2);
    }
}