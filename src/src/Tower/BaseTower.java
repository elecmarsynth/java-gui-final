package Tower;
import java.awt.Color;
import java.awt.Graphics2D;

import UI.UpgradeTower;

public class BaseTower extends PlantTower implements UpgradeTower {
    private int level = 1;
    private final int MAX_LEVEL = 5;
    private int upgradeCost = 100;

    public BaseTower(int col, int row) {
        super(col, row);
        this.maxHp = 20000;
        this.hp = maxHp;
    }

    @Override
    public int getLevel() { 
        return level; 
    }
    @Override
    public boolean upgrade() {
        if (level < MAX_LEVEL) {
            level++;
            maxHp += 500;
            hp = maxHp;
            upgradeCost += 150;
            return true;
        }
        return false;
    }
    public String getName() { 
        return "Base Tower"; 
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
        g2.setColor(Color.BLUE);
        g2.fillRect(x + 5, y + 5, tileSize - 10, tileSize - 10);
        g2.setColor(Color.WHITE);
        g2.drawString("Lv." + level, x + 10, y + 25);
    }
}