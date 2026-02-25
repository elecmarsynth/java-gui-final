package Tower;
import java.awt.Color;
import java.awt.Graphics2D;
import Enemy.Enemy;

public class Bullet {
    private double x, y; 
    private Enemy target;
    private double speed = 10.0; 
    public boolean isActive = true;
    private int damage;
    public Bullet(double x, double y, Enemy target,int damage) {
        this.x = x;
        this.y = y;
        this.target = target;
        this.damage = damage;
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
            target.takeDamage(this.damage); 
            isActive = false; 
        } else {
            x += (diffX / distance) * speed;
            y += (diffY / distance) * speed;
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.YELLOW);
        g2.fillOval((int)x - 4, (int)y - 4, 8, 8); 
    }
}