package UI;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ImageLoader {
    public static BufferedImage turret;
    public static BufferedImage miner;
    public static BufferedImage base;
    public static BufferedImage goldMine;
    public static BufferedImage enemyNormal;
    public static BufferedImage enemyTank;
    public static BufferedImage enemySpeed;

    public static void loadAll() {
        try {
            turret      = ImageIO.read(ImageLoader.class.getResourceAsStream("/image/Turret.png"));
            miner       = ImageIO.read(ImageLoader.class.getResourceAsStream("/image/Sunflower.png"));
            base        = ImageIO.read(ImageLoader.class.getResourceAsStream("/image/WorldTree.png"));
            goldMine    = ImageIO.read(ImageLoader.class.getResourceAsStream("/image/Sun.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
