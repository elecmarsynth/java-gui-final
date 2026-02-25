package UI;
public interface UpgradeTower {
    boolean upgrade();     
    int getUpgradeCost(); 
    int getLevel(); 
    boolean isMaxLevel(); 
    String getName();
}