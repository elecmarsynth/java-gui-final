
public class Coins {
    private int coins = 100 ; //เงินตอนเริ่มเกม

    Coins() {
    }

    public int getCoins() {
        return coins; }

    public void addCoins(int amount) { 
        coins += amount; }

    public boolean spendCoins(int amount) {
        if (coins >= amount) {
            coins -= amount;
            return true;
        }
        return false;
    }
}