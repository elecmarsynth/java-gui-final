import java.awt.*;

/**
 * มอนสเตอร์หลักของเกม รองรับทุก MonsterType
 * รวม Farmer.java เข้ามาแล้ว (ลบ Farmer.java ออกได้เลย)
 */
public class rpgMonster extends Enemy {
    private MonsterType type;

    public rpgMonster(double x, double y, MonsterType type, int waveLevel) {
        super(
            x, y,
            type == MonsterType.TANK ? 35 : 25,                          // size
            (1.0 + waveLevel * 0.1) * type.speedMultiplier,              // speed
            (int) ((10 + waveLevel * 5) * type.hpMultiplier)             // maxHp
        );
        this.type = type;
    }

    @Override
    public void draw(Graphics2D g2) {
        // วาดตัวมอนสเตอร์ (สีตาม MonsterType)
        g2.setColor(type.color);
        g2.fillRect((int) x, (int) y, size, size);

        // วาดขอบ
        g2.setColor(Color.BLACK);
        g2.drawRect((int) x, (int) y, size, size);

        // วาด HP Bar
        g2.setColor(Color.BLACK);
        g2.fillRect((int) x, (int) y - 10, size, 5);
        g2.setColor(Color.GREEN);
        int hpBarWidth = (int) (((double) hp / maxHp) * size);
        g2.fillRect((int) x, (int) y - 10, hpBarWidth, 5);
    }
}