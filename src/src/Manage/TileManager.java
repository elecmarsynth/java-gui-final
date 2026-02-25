package Manage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage; 
import java.io.IOException;      
import java.util.ArrayList;        
import javax.imageio.ImageIO;

import Tower.GoldMine;
import UI.ImageLoader;
import panelCore.GamePanel;

public class TileManager {
    
    GamePanel gp;
    public int[][] mapData;
    public BufferedImage grassImage; 
    public ArrayList<GoldMine> goldMines = new ArrayList<>();
    public TileManager(GamePanel gp) {
        this.gp = gp;
        mapData = new int[gp.getMaxRow()][gp.getMaxCol()];
        getTileImage();
        
        setupMap();
    }
    public void getTileImage() {
        try {
            grassImage = ImageIO.read(getClass().getResourceAsStream("/image/a.png"));
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }

    public void setupMap() {
        for (int r = 0; r < gp.getMaxRow(); r++) {
            for (int c = 0; c < gp.getMaxCol(); c++) {
                 if (r == 0 || r == gp.getMaxRow() - 1 || c == 0 || c == gp.getMaxCol() - 1) mapData[r][c] = 9; 
                 else if (r == gp.getMaxRow() / 2 && c == gp.getMaxCol() / 2) mapData[r][c] = 1; 
                 else mapData[r][c] = 0; 
            }
        }
        placeMines();
    }
    private void placeMines() {
        int minesToPlace = 3;
        while (minesToPlace > 0) {
            int r = (int)(Math.random() * (gp.getMaxRow() - 2)) + 1;
            int c = (int)(Math.random() * (gp.getMaxCol() - 2)) + 1;
            if (mapData[r][c] == 0 && (Math.abs(r - gp.getMaxRow()/2) + Math.abs(c - gp.getMaxCol()/2) > 2)) {
                mapData[r][c] = 2;
                goldMines.add(new GoldMine(c, r)); 
                minesToPlace--;
        }
    }
}
    public ArrayList<GoldMine> getGoldMines() {
    return goldMines;
}
    public void draw(Graphics2D g2) {
    for (int r = 0; r < gp.getMaxRow(); r++) {
        for (int c = 0; c < gp.getMaxCol(); c++) {
            int tileType = mapData[r][c];
            int x = c * gp.getTileSize();
            int y = r * gp.getTileSize();
            if (grassImage != null) {
                g2.drawImage(grassImage, x, y, gp.getTileSize(), gp.getTileSize(), null);
            } else {
                g2.setColor(new Color(34, 139, 34));
                g2.fillRect(x, y, gp.getTileSize(), gp.getTileSize());
            }
            if (tileType == 9) {
                g2.setColor(new Color(60, 60, 60, 180)); 
                g2.fillRect(x, y, gp.getTileSize(), gp.getTileSize());
            } else if (tileType == 2) {
                if (ImageLoader.goldMine != null) {
                    g2.drawImage(ImageLoader.goldMine, x, y, gp.getTileSize(), gp.getTileSize(), null);
                } else {
                    g2.setColor(Color.ORANGE);
                    g2.fillOval(x + 5, y + 5, gp.getTileSize() - 10, gp.getTileSize() - 10);
                }
            }
            g2.setColor(new Color(0, 0, 0, 40));
            g2.drawRect(x, y, gp.getTileSize(), gp.getTileSize());
        }
    }
}
}