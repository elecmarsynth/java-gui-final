import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Base class สำหรับ object ทั่วไปในเกม (ไม่ใช่ Enemy)
 * เช่น Projectile, Item, ฯลฯ
 */
public abstract class Entity {
    protected double x, y;
    protected int size;
    protected double speed;

    public Entity(double x, double y, int size, double speed) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
    }

    public abstract void update();
    public abstract void draw(Graphics2D g2);

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, size, size);
    }
}