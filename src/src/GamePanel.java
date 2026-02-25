import java.awt.*;
import java.awt.event.*; 
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {
    private int tileSize;
    private int maxCol = 22;
    private int maxRow = 11;
    private int screenWidth;
    private int screenHeight; 
    private Thread gameThread;
    private TileManager tileM;
    private EnemyManager enemyM;
    private TowerManager towerM;
    private GameWindow window;
    private int selectedTowerType = 0;
    public static final int TURRET_COST = 20;  // add ราคา
    public static final int MINER_COST = 10;  // add ราคา
    Coins coin_test = new Coins();   //add
    WaveManager waveM = new WaveManager();
    DisPlayTime hud = new DisPlayTime();


    public GamePanel(GameWindow window) {
        this.window = window;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int realScreenWidth  = screenSize.width;
        int realScreenHeight = screenSize.height;

        // ✅ คำนวณ tileSize ให้พอดีกับจอ (เลือกค่าที่เล็กกว่าเพื่อไม่ให้ล้น)
        int tileSizeByWidth  = realScreenWidth  / maxCol;
        int tileSizeByHeight = realScreenHeight / maxRow;
        tileSize = Math.min(tileSizeByWidth, tileSizeByHeight);

        // ✅ คำนวณขนาดจริงที่ใช้
        screenWidth  = tileSize * maxCol;
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
                int col = e.getX() / tileSize;
                int row = e.getY() / tileSize;
                PlantTower clickedTower = towerM.getTowerAt(col, row);
                if (clickedTower != null) {
                window.showUpgrade(clickedTower);
                setSelectedTower(0);
            } 
            // 3. ถ้าไม่เจอ (คลิกพื้นว่าง) ให้กลับไปหน้าเลือกป้อมปกติ
                else {
                    if (selectedTowerType != 0) {
                        int cost = (selectedTowerType == 1) ? TURRET_COST : MINER_COST; //add ตัดเงิน
                        if (coin_test.spendCoins(cost)) {
                            towerM.addTower(col, row, selectedTowerType);
                        } else {
                            System.out.println("out of money");
                        } // จนถึงตรงนี้
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
        while(gameThread != null) {
            update();
            repaint();
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000.0;
                if(remainingTime < 0) remainingTime = 0;
                Thread.sleep((long)remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }
    public void update() {
    enemyM.update();
    towerM.update(enemyM.enemies);
    waveM.update(enemyM.enemies, screenWidth, screenHeight); // ✅ เพิ่ม
    if (towerM.isBaseDestroyed()) {
        gameThread = null; // หยุด game loop
        System.out.println("GAME OVER");
    }
    }

    public void setSelectedTower(int type) {
        this.selectedTowerType = type;
    }
    public int getTileSize(){
        return this.tileSize;
    }
    public int getMaxRow(){
        return this.maxRow;
    }
    public int getMaxCol(){
        return this.maxCol;
    }
    public int getScreenWidth(){
        return this.screenWidth;
    }
    public int getScreenHeight(){
        return this.screenHeight;
    }
    public TileManager getTileM() {
    return tileM;
    }
    public EnemyManager getEnemyM(){
        return enemyM;
    }
    public TowerManager getTowerM() {
    return towerM;
    }
    public GameWindow getWindow(){
        return window;
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
        g2.drawString("💵 " + coin_test.getCoins(), 20, 40);
        hud.draw(g2, waveM, enemyM.enemies.size(), screenWidth);
        g2.dispose();
    }
}