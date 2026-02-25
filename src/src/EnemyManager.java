import java.awt.Graphics2D;
import java.util.ArrayList;

public class EnemyManager {
    GamePanel gp;
    public ArrayList<Enemy> enemies = new ArrayList<>();
    int spawnCounter = 0;

    public EnemyManager(GamePanel gp) {
        this.gp = gp;
    }

    public void update() {
    for (int i = 0; i < enemies.size(); i++) {
        Enemy e = enemies.get(i);
        e.update(gp);
        if (e.isDead()) {
            enemies.remove(i);
            i--;
        }
    }
    gp.getTowerM().removeDestroyedTowers();
    }
    public void draw(Graphics2D g2) {
        for (Enemy e : enemies) e.draw(g2);
    }
}