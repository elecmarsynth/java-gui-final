package Tower;
import UI.Coins;
import UI.ImageLoader;
import UI.UpgradeTower;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Miner extends PlantTower implements UpgradeTower {
    private int level = 1;
    private final int MAX_LEVEL = 3; // เหมืองอัปได้แค่ 3 เลเวล
    private int upgradeCost = 100;   // เหมืองอัปแพงกว่าป้อมปืน
    private int timer = 0;
    public Miner(int col, int row) {
        super(col, row);
        // ตั้งค่าเฉพาะของเครื่องขุด
        this.maxHp = 100; // เครื่องขุดบอบบางกว่า
        this.hp = maxHp;
    }
    public void collectGold(ArrayList<GoldMine> mines, Coins coins) {
    timer++;
    if (timer >= 300) {
        for (GoldMine mine : mines) {
            if ((Math.abs(this.col - mine.col) == 1 && this.row == mine.row) ||
                (Math.abs(this.row - mine.row) == 1 && this.col == mine.col)) {
                int earned = (level == 1) ? 10 : (level == 2) ? 15 : 20;
                coins.addCoins(earned); // ✅ เพิ่มเงินจริงๆ
            }
        }
        timer = 0;
    }
    }
    public String getName() { 
        return "Gold Miner"; 
    }
    public int getUpgradeCost() { 
        return 200 * level; 
    }
    @Override
    public boolean upgrade() {
        if (isMaxLevel()){ 
            return false;
        }
        level++;
        if (level == 2) {
            this.maxHp += 50;
        } else if (level == 3) {
            this.maxHp += 100;
            this.upgradeCost = 0;
        }
        this.hp = this.maxHp;
        return true;
    }
    @Override
    public int getLevel() { 
        return level; 
    }
    @Override
    public boolean isMaxLevel() { 
        return level >= MAX_LEVEL; 
    }   
    @Override
    public void draw(Graphics2D g2, int tileSize) {
        int x = col * tileSize;
        int y = row * tileSize;

        if (ImageLoader.miner != null) {
        g2.drawImage(ImageLoader.miner, x, y, tileSize, tileSize, null);
    } else {
        g2.setColor(Color.RED);
        g2.fillRect(x + 5, y + 5, tileSize, tileSize);
    }
    }
}