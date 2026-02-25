package Tower;
import java.awt.Graphics2D;
import java.awt.Rectangle;
public abstract class PlantTower {
    protected int col, row;
    protected int hp, maxHp;
    protected double x, y;
    protected int totalSpent; 
    
    
    public PlantTower(int col, int row) {
        this.col = col;
        this.row = row;
        this.maxHp = 100;
        this.hp = maxHp;
    }

    public void addSpent(int amount) {
        totalSpent += amount;
    }

    public int getTotalSpent() {
        return totalSpent;
    }
    public abstract void draw(Graphics2D g2, int tileSize);
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y); 
    }
    public boolean isDestroyed() {
        return hp <= 0; 
    }
    
    public void takeDamage(int damage) {
        hp -= damage; 
    }
    public int getCol() { 
        return col; 
    }
    public int getRow() { 
        return row; 
    }
    
}