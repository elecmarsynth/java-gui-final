package UI;
public interface UpgradeTower {
    // ฟังก์ชันสำหรับเรียกเพื่ออัปเกรด (ส่งกลับค่า true ถ้าอัปสำเร็จ)
    boolean upgrade();     
    // ดูราคาอัปเกรดเพื่อไปเลเวลถัดไป
    int getUpgradeCost(); 
    // ดูเลเวลปัจจุบันของป้อม
    int getLevel(); 
    // เช็คว่าเลเวลตันหรือยัง (จะได้ซ่อนปุ่มอัปเกรด หรือป้องกันการกดซ้ำ)
    boolean isMaxLevel(); 
    String getName();
}