package Tower;
import java.awt.Color;
import java.awt.Graphics2D;

import Enemy.Enemy;

public class Bullet {
    private int x, y;
    private Enemy target;
    private int speed = 10;
    public boolean isActive = true;

    public Bullet(int x, int y, Enemy target) {
        this.x = x;
        this.y = y;
        this.target = target;
    }
    public void update() {
        if (target == null || target.isDead()) {
            isActive = false;
            return;
        }
        double diffX = target.getX() - x;
        double diffY = target.getY() - y;
        double distance = Math.sqrt(diffX*diffX + diffY*diffY);
        if (distance <= speed) {
            target.takeDamage(10); 
            isActive = false; 
        } else {
            x += (diffX / distance) * speed;
            y += (diffY / distance) * speed;
        }
    }
    public void draw(Graphics2D g2) {
        g2.setColor(Color.YELLOW);
        g2.fillOval(x - 4, y - 4, 8, 8);
    }
}