package Gameover;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class GameOver {

      public void draw(Graphics2D g2, int width, int height) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, 0, width, height);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 60));

            String text = "GAME OVER";
            FontMetrics metrics = g2.getFontMetrics();
            int x = (width - metrics.stringWidth(text)) / 2;
            int y = (height / 2);

            g2.drawString(text, x, y);

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            String subText = "The Base has been destroyed!";
            int subX = (width - g2.getFontMetrics().stringWidth(subText)) / 2;
            g2.drawString(subText, subX, y + 50);
      }
}