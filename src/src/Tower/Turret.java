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
    private int upgradeCost = 50; 

    public Turret(int col, int row) {
        super(col, row);
        this.maxHp = 200;
        
    }
    public void update(ArrayList<Enemy> enemies, int tileSize) {
    // อัปเดตกระสุนเหมือนเดิม
    for (int i = 0; i < bullets.size(); i++) {
        bullets.get(i).update();
        if (!bullets.get(i).isActive) {
            bullets.remove(i);
            i--;
        }
    }
   
    if (cooldown > 0) { cooldown--; return; }
    int centerX = (col * tileSize) + (tileSize / 2);
    int centerY = (row * tileSize) + (tileSize / 2);
    int range = 200; 

    for (Enemy e : enemies) {
        double dist = Math.hypot(e.getX() - centerX, e.getY() - centerY);
        if (dist < range) { 
            bullets.add(new Bullet(centerX, centerY, e));
            cooldown = 30; 
            break; 
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
            return false; 
        }
        level++;
        if (level == 2) {
            this.maxHp += 100;       
            this.upgradeCost = 150;  
        } else if (level == 3) {
            this.maxHp += 200;
            this.upgradeCost = 0;    
        }
        this.hp = this.maxHp; 
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