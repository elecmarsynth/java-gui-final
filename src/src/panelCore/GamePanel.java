package panelCore;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;

import MainMenu.App;
import Manage.EnemyManager;
import Manage.TileManager;
import Manage.TowerManager;
import Manage.WaveManager;
import Tower.PlantTower;
import UI.Coins;
import UI.DisPlayTime;
import frame.GameWindow;

public class GamePanel extends JPanel implements Runnable {
    private int tileSize;
    private int maxCol = 30;
    private int maxRow = 15;
    private int screenWidth;
    private int screenHeight;
    private Thread gameThread;
    private TileManager tileM;
    private EnemyManager enemyM;
    private TowerManager towerM;
    private GameWindow window;
    private int selectedTowerType = 0;
    private final int TURRET_COST = 20;
    private final int MINER_COST = 10;
    private Coins coins = new Coins();
    WaveManager waveM = new WaveManager();
    DisPlayTime hud = new DisPlayTime();
    public boolean isGameOver = false;
    private Gameover.GameOver gameOverScene = new Gameover.GameOver();

    public GamePanel(GameWindow window) {
        this.window = window;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int realScreenWidth = screenSize.width;
        int realScreenHeight = screenSize.height;
        int tileSizeByWidth = realScreenWidth / maxCol;
        int tileSizeByHeight = realScreenHeight / maxRow;
        tileSize = Math.min(tileSizeByWidth, tileSizeByHeight);
        screenWidth = tileSize * maxCol;
        screenHeight = tileSize * maxRow;
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        tileM = new TileManager(this);
        enemyM = new EnemyManager(this);
        towerM = new TowerManager(this);
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isGameOver) {
                    restartGame();
                    return;
                }

                int col = e.getX() / tileSize;
                int row = e.getY() / tileSize;
                PlantTower clickedTower = towerM.getTowerAt(col, row);

                if (clickedTower != null) {
                    window.showUpgrade(clickedTower);
                    setSelectedTower(0);
                } else {
                    if (selectedTowerType != 0) {
                        int cost = (selectedTowerType == 1) ? TURRET_COST : MINER_COST;
                        if (coins.spendCoins(cost)) {
                            towerM.addTower(col, row, selectedTowerType);
                        } else {
                            System.out.println("out of money");
                        }
                        setSelectedTower(0);
                    } else {
                        window.showTower();
                    }
                }
            }
        });
        startGameThread();
        waveM.start(enemyM.enemies, screenWidth, screenHeight);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / 60;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null) {
            update();
            repaint();
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000.0;
                if (remainingTime < 0)
                    remainingTime = 0;
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (!isGameOver) {
            enemyM.update();
            towerM.update(enemyM.enemies);
            waveM.update(enemyM.enemies, screenWidth, screenHeight);

            if (towerM.isBaseDestroyed()) {
                isGameOver = true;
                System.out.println("GAME OVER");

                playSound("panelCore/GameOverSound.wav");
            }
        }
    }

    public void setSelectedTower(int type) {
        this.selectedTowerType = type;
    }

    public int getTileSize() {
        return this.tileSize;
    }

    public int getMaxRow() {
        return this.maxRow;
    }

    public int getMaxCol() {
        return this.maxCol;
    }

    public int getScreenWidth() {
        return this.screenWidth;
    }

    public int getScreenHeight() {
        return this.screenHeight;
    }

    public TileManager getTileM() {
        return tileM;
    }

    public EnemyManager getEnemyM() {
        return enemyM;
    }

    public TowerManager getTowerM() {
        return towerM;
    }

    public GameWindow getWindow() {
        return window;
    }

    public Coins getCoins() {
        return coins;
    }

    public int getTurretCost() {
        return TURRET_COST;
    }

    public int getMinerCost() {
        return MINER_COST;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        tileM.draw(g2);
        towerM.draw(g2);
        enemyM.draw(g2);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Phai", Font.BOLD, 24));
        g2.drawString("💵 " + coins.getCoins(), 20, 40);
        hud.draw(g2, waveM, enemyM.enemies.size(), screenWidth);

        if (isGameOver) {
            gameOverScene.draw(g2, screenWidth, screenHeight);
        }

        g2.dispose();
    }

    public void playSound(String filePath) {
        try {
            InputStream sound = GamePanel.class.getClassLoader().getResourceAsStream(filePath);

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(sound);

            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void restartGame() {
        this.coins = new Coins();
        this.enemyM = new EnemyManager(this);
        this.towerM = new TowerManager(this);
        this.waveM = new WaveManager();
        this.tileM = new TileManager(this);

        this.selectedTowerType = 0;
        this.isGameOver = false;

        waveM.start(enemyM.enemies, screenWidth, screenHeight);
    }
}