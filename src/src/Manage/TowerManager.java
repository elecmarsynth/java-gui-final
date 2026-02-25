package Manage;
import java.awt.Graphics2D;
import java.util.ArrayList;
import Enemy.Enemy;
import Tower.BaseTower;
import Tower.Miner;
import Tower.PlantTower;
import Tower.Turret;
import panelCore.GamePanel;

public class TowerManager {
    private GamePanel gp;
    private ArrayList<PlantTower> towers = new ArrayList<>();

    public TowerManager(GamePanel gp) {
        this.gp = gp;
        int baseCol = gp.getMaxCol() / 2;
        int baseRow = gp.getMaxRow() / 2;
        towers.add(new BaseTower(baseCol, baseRow));
    }
    public boolean isBaseDestroyed() {
    for (PlantTower t : towers) {
        if (t instanceof BaseTower && t.isDestroyed()) return true;
    }
    return false;
    }
    // TowerManager.java
    public PlantTower getTowerAt(int col, int row) {
        for (PlantTower t : towers) {
            if (t.getCol() == col && t.getRow() == row) {
                return t;
            }
        }
        return null;
    }
    public void addTower(int col, int row, int type) {
    if (gp.getTileM().mapData[row][col] == 0) { 
        if (type == 1) { // Turret
            Turret t = new Turret(col, row);
            t.addSpent(gp.getTurretCost()); // ✅ บันทึกราคาซื้อ
            towers.add(t);
            gp.getTileM().mapData[row][col] = 3;
        } else if (type == 2) { // Miner
            if (isAdjacentToGoldMine(col, row)) {
                Miner m = new Miner(col, row);
                m.addSpent(gp.getMinerCost()); // ✅ บันทึกราคาซื้อ
                towers.add(m);
                gp.getTileM().mapData[row][col] = 3;
            } else {
                System.out.println("Can only place Miner near Gold Mine!");
            }
        }
    }
}

private boolean isAdjacentToGoldMine(int col, int row) {
    // วนลูปเช็คใน TileManager ว่าช่องรอบๆ เป็นเหมืองทอง (Type 2) หรือไม่
    int[][] data = gp.getTileM().mapData;
    if (data[row-1][col] == 2 || data[row+1][col] == 2 || 
        data[row][col-1] == 2 || data[row][col+1] == 2) return true;
    return false;
}
    public void update(ArrayList<Enemy> enemies) {
    for (PlantTower t : towers) {
        if (t instanceof Turret) {
            ((Turret) t).update(enemies);
        }
        // ✅ เพิ่ม
        if (t instanceof Miner) {
            ((Miner) t).collectGold(gp.getTileM().getGoldMines(), gp.getCoins());
        }
    }
}
    public void sellTower(PlantTower tower) { //ขายทิ้ง
    towers.remove(tower);
    }
    public PlantTower getTowerBlockingPath(Enemy enemy, double targetX, double targetY, int tileSize) {
    // คำนวณทิศทางที่ enemy จะเดิน
    double diffX = targetX - enemy.getX();
    double diffY = targetY - enemy.getY();
    double distance = Math.sqrt(diffX * diffX + diffY * diffY);
    if (distance == 0) return null;

    // ดู tile ถัดไปที่ enemy จะเหยียบ (มองไปข้างหน้า 1 step)
    double nextX = enemy.getX() + (diffX / distance) * tileSize;
    double nextY = enemy.getY() + (diffY / distance) * tileSize;
    int nextCol = (int)(nextX / tileSize);
    int nextRow = (int)(nextY / tileSize);

    return getTowerAt(nextCol, nextRow); // ใช้ method เดิมที่มีอยู่แล้ว
}
public void removeDestroyedTowers() {
    for (PlantTower t : towers) {
        if (t.isDestroyed() && !(t instanceof BaseTower)) {
            // ✅ คืน tile กลับเป็นหญ้า
            gp.getTileM().mapData[t.getRow()][t.getCol()] = 0;
        }
    }
    towers.removeIf(t -> t.isDestroyed() && !(t instanceof BaseTower));
}
    public void draw(Graphics2D g2) {
        for (PlantTower t : towers) t.draw(g2, gp.getTileSize());
    }
}