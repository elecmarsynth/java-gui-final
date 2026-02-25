package Manage;
import java.util.ArrayList;
import java.util.Random;

import Enemy.Enemy;
import Enemy.MonsterType;
import Enemy.rpgMonster;

public class WaveManager {
    private int currentWave = 0;
    private long lastWaveTime;
    private long waveCooldown = 60000;
    private boolean isStarted = false;

    public WaveManager() {}

    public void start(ArrayList<Enemy> monsters, int w, int h) {
        if (isStarted) return;
        isStarted = true;
        lastWaveTime = System.currentTimeMillis();
        startNextWave(monsters, w, h);
    }

    public void update(ArrayList<Enemy> monsters, int screenWidth, int screenHeight) {
        if (!isStarted) return;

        long timePassed = System.currentTimeMillis() - lastWaveTime;
        if (timePassed >= waveCooldown) {
            startNextWave(monsters, screenWidth, screenHeight);
            lastWaveTime = System.currentTimeMillis();
        }
    }

    public void startNextWave(ArrayList<Enemy> monsters, int w, int h) {
        currentWave++;
        System.out.println(">>> Starting Wave " + currentWave);

        int monsterCount = 3 + (currentWave * 2);
        Random rand = new Random();

        for (int i = 0; i < monsterCount; i++) {
            // สุ่มตำแหน่ง spawn รอบขอบจอ
            double spawnX, spawnY;
            if (rand.nextBoolean()) {
                spawnX = rand.nextBoolean() ? -50 : w + 50;
                spawnY = rand.nextInt(h);
            } else {
                spawnX = rand.nextInt(w);
                spawnY = rand.nextBoolean() ? -50 : h + 50;
            }

            // สุ่มประเภทมอนสเตอร์: TANK 20%, SPEED 30%, NORMAL 50%
            int chance = rand.nextInt(100);
            MonsterType spawnType;
            if (chance < 20)      spawnType = MonsterType.TANK;
            else if (chance < 50) spawnType = MonsterType.SPEED;
            else                  spawnType = MonsterType.NORMAL;

            // ✅ ใช้ rpgMonster จริงๆ แทน Farmer
            monsters.add(new rpgMonster(spawnX, spawnY, spawnType, currentWave));
        }
    }

    public boolean isGameStarted() { return isStarted; }
    public int getWaveNumber()     { return currentWave; }

    public long getTimeLeft() {
        if (!isStarted) return waveCooldown;
        long left = waveCooldown - (System.currentTimeMillis() - lastWaveTime);
        return Math.max(left, 0);
    }
}