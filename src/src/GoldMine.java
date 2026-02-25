import java.awt.Color;
import java.awt.Graphics2D;

public class GoldMine {
    public int col, row;
    public int goldAmount = 5000; // ปริมาณทองที่มีในเหมือง

    public GoldMine(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public void draw(Graphics2D g2, int tileSize) {
        int x = col * tileSize;
        int y = row * tileSize;
        g2.setColor(Color.ORANGE);
        g2.fillOval(x + 5, y + 5, tileSize - 10, tileSize - 10);
        g2.setColor(Color.BLACK);
        g2.drawString("GOLD", x + 15, y + tileSize/2);
    }
}