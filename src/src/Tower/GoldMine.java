package Tower;
import java.awt.Color;
import java.awt.Graphics2D;

import UI.ImageLoader;

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
        if (ImageLoader.goldMine != null) {
        g2.drawImage(ImageLoader.goldMine, x, y, tileSize, tileSize, null);
    } else {
        g2.setColor(Color.ORANGE);
        g2.fillOval(x + 5, y + 5, tileSize - 10, tileSize - 10);
    }
    }
}