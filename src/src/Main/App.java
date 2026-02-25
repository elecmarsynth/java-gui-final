package Main;
import javax.swing.JPanel;

import MainMenu.LoginGameApp;
import frame.GameWindow;

public class App {
    public static void main(String[] args) {
        new LoginGameApp();
    }
    public static JPanel StartGamePanel(){
        new GameWindow();
        return null;
    }
}



