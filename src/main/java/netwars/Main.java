package netwars;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameStartScreen startScreen = new GameStartScreen();
            startScreen.setVisible(true);
        });
    }
}
