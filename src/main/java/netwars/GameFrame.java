package netwars;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    GamePanel panel;

    /* This is the code regarding the frame of the game */

    GameFrame() {
        panel = new GamePanel();
        this.add(panel);
        this.setTitle("Net Wars: Rivalry on the Table");
        this.setResizable(false);
        this.setBackground(Color.BLACK);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }
}
