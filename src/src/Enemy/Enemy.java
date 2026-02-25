package Enemy;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import Tower.PlantTower;
import panelCore.GamePanel;
public abstract class Enemy {
    protected double x, y;
    protected int size;
    protected double speed;
    protected int maxHp;
    protected int hp;

    public Enemy(double x, double y, int size, double speed, int maxHp) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
        this.maxHp = maxHp;
        this.hp = maxHp;
    }

    public void update(GamePanel gp) {
        double targetX = (gp.getMaxCol() / 2.0) * gp.getTileSize();
        double targetY = (gp.getMaxRow() / 2.0) * gp.getTileSize();

        PlantTower blockedTower = gp.getTowerM().getTowerBlockingPath(this, targetX, targetY, gp.getTileSize());
        if (blockedTower != null) {
            blockedTower.takeDamage(1);
        } else {
            double diffX = targetX - x;
            double diffY = targetY - y;
            double distance = Math.sqrt(diffX * diffX + diffY * diffY);
            if (distance > 5) {
                double currentSpeed = (gp.getTileSize() / 64.0) * speed;
                x += (diffX / distance) * currentSpeed;
                y += (diffY / distance) * currentSpeed;
            }
        }
    }
    public abstract void draw(Graphics2D g2);
    public void takeDamage(int dmg) {
        this.hp -= dmg;
    }
    public boolean isDead() {
        return hp <= 0;
    }
    public double getX() { 
        return x + size / 2.0;
     }
    public double getY() { 
        return y + size / 2.0;
     }
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, size, size);
    }
}