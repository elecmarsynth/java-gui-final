package Manage;

import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JOptionPane;

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
            if (t instanceof BaseTower && t.isDestroyed())
                return true;
        }
        return false;
    }

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
            if (type == 1) {
                Turret t = new Turret(col, row);
                t.addSpent(gp.getTurretCost());
                towers.add(t);
                gp.getTileM().mapData[row][col] = 3;
            } else if (type == 2) {
                if (isAdjacentToGoldMine(col, row)) {
                    Miner m = new Miner(col, row);
                    m.addSpent(gp.getMinerCost());
                    towers.add(m);
                    gp.getTileM().mapData[row][col] = 3;
                } else {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(gp,
                                "ต้องอยู่ใกล้ๆกับแร่ทองเท่านัน! \nคุณโดนหักค่าซีเลง จำนวน 10 coins",
                                "Information",
                                JOptionPane.INFORMATION_MESSAGE);
                    });
                    System.out.println("ต้องอยู่ใกล้ ๆกับแร่ทองเท่านัน! \nคุณโดนหักค่าซีเลง จำนวน 10 coins");
                }
            }
        }
    }

    private boolean isAdjacentToGoldMine(int col, int row) {
        int[][] data = gp.getTileM().mapData;
        if (data[row - 1][col] == 2 || data[row + 1][col] == 2 ||
                data[row][col - 1] == 2 || data[row][col + 1] == 2)
            return true;
        return false;
    }

    public void update(ArrayList<Enemy> enemies) {
        for (PlantTower t : towers) {
            if (t instanceof Turret) {
                ((Turret) t).update(enemies, gp.getTileSize());
            }
            if (t instanceof Miner) {
                ((Miner) t).collectGold(gp.getTileM().getGoldMines(), gp.getCoins());
            }
        }
    }

    public void sellTower(PlantTower tower) {
        if (tower instanceof BaseTower)
            return;
        towers.remove(tower);
    }

    public PlantTower getTowerBlockingPath(Enemy enemy, double targetX, double targetY, int tileSize) {
        double diffX = targetX - enemy.getX();
        double diffY = targetY - enemy.getY();
        double distance = Math.sqrt(diffX * diffX + diffY * diffY);
        if (distance == 0)
            return null;
        double nextX = enemy.getX() + (diffX / distance) * tileSize;
        double nextY = enemy.getY() + (diffY / distance) * tileSize;
        int nextCol = (int) (nextX / tileSize);
        int nextRow = (int) (nextY / tileSize);
        return getTowerAt(nextCol, nextRow);
    }

    public void removeDestroyedTowers() {
        for (PlantTower t : towers) {
            if (t.isDestroyed()) {
                if (!(t instanceof BaseTower)) {
                    gp.getTileM().mapData[t.getRow()][t.getCol()] = 0;
                } else {
                    gp.isGameOver = true;
                }
            }
        }
        towers.removeIf(t -> t.isDestroyed() && !(t instanceof BaseTower));
    }

    public void draw(Graphics2D g2) {
        for (PlantTower t : towers)
            t.draw(g2, gp.getTileSize());
    }
}