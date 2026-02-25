import java.awt.*;

public class DisPlayTime {
    public void draw(Graphics2D g2, WaveManager waveM, int enemyCount, int screenWidth) {
        g2.setFont(new Font("Arial", Font.BOLD, 20));

        // Wave number - ซ้ายบน
        g2.setColor(Color.YELLOW);
        g2.drawString("🌊 Wave: " + waveM.getWaveNumber(), 20, 70);

        // จำนวน Enemy - กลางบน
        g2.setColor(Color.RED);
        String enemyText = "👾 Enemies: " + enemyCount;
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(enemyText);
        g2.drawString(enemyText, (screenWidth - textWidth) / 2, 40);

        // เวลาถึง Wave ถัดไป - ขวาบน
        long timeLeft = waveM.getTimeLeft() / 1000; // แปลงเป็นวินาที
        g2.setColor(Color.CYAN);
        String timerText = "⏱ Next Wave: " + timeLeft + "s";
        int timerWidth = fm.stringWidth(timerText);
        g2.drawString(timerText, screenWidth - timerWidth - 20, 40);
    }
}