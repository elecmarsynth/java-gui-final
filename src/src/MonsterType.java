import java.awt.Color;
public enum MonsterType {
    NORMAL(1.0, 1.0, Color.RED),
    TANK(2.5, 0.5, Color.ORANGE),
    SPEED(0.8, 1.5, Color.CYAN);

    public final double hpMultiplier;
    public final double speedMultiplier;
    public final Color color;

    MonsterType(double hpMultiplier, double speedMultiplier, Color color) {
        this.hpMultiplier = hpMultiplier;
        this.speedMultiplier = speedMultiplier;
        this.color = color;
    }
}