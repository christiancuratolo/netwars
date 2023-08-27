package netwars;

import javax.swing.*;
import java.awt.*;

/*This is the code regarting the StartUp Screen of the game */

public class GameStartScreen extends JFrame {

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    public GameStartScreen() {
        setTitle("NetWars: Rivalry on the Table");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel("NetWars: Rivalry on the Table");
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));

        JButton startButton = new JButton("Start Game");
        startButton.setForeground(Color.YELLOW);
        startButton.setBackground(Color.BLACK);
        startButton.setFont(new Font("Arial", Font.PLAIN, 18));
        startButton.setFocusPainted(false);
        startButton.addActionListener(e -> {
            dispose();
            GameFrame gameFrame = new GameFrame();
            gameFrame.setVisible(true);
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(50, 0, 20, 0);
        panel.add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 50, 0);
        panel.add(startButton, gbc);

        getContentPane().add(panel);
    }

}

