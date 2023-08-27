package netwars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {

    static final int GAME_WIDTH = 1200;
    static final int GAME_HEIGHT = (int) (GAME_WIDTH * (0.56));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;
    private static final int MAX_SCORE = 11;
    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Score score;
    boolean gameOver = false;
    boolean finalScreen = false;

    GamePanel() {
        newPaddles();
        newBall();
        score = new Score(GAME_WIDTH, GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void newBall() {
        random = new Random();
        ball = new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), random.nextInt(GAME_HEIGHT - BALL_DIAMETER), BALL_DIAMETER, BALL_DIAMETER);
    }

    public void newPaddles() {
        paddle1 = new Paddle(0, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
        paddle2 = new Paddle(GAME_WIDTH - PADDLE_WIDTH, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 2);

    }

    public void paint(Graphics g) {
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }

    public void draw(Graphics g) {
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);

        /*The code regarding the final screen */

        if (finalScreen) {
            String winnerText = (score.player1 >= MAX_SCORE) ? "Player 1 Wins!" : "Player 2 Wins!";
            String restartText = "Press 'R' to Play Again";
            String quitText = "Press 'Q' to Quit";

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString(winnerText, GAME_WIDTH / 2 - 150, GAME_HEIGHT / 2);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString(restartText, GAME_WIDTH / 2 - 100, GAME_HEIGHT / 2 + 40);
            g.drawString(quitText, GAME_WIDTH / 2 - 80, GAME_HEIGHT / 2 + 70);
        }
    }

    public void move() {
        paddle1.move();
        paddle2.move();
        ball.move();
    }


    /*Check collision: the physique of the game */
    public void checkCollision() {

        /* Bounce ball off top & bottom window edges */
        if (ball.y <= 0) {
            ball.setYDirection(-ball.yVelocity);
        }
        if (ball.y >= GAME_HEIGHT - BALL_DIAMETER) {
            ball.setYDirection(-ball.yVelocity);
        }

        /*This bounce ball of paddles */
        if (ball.intersects(paddle1)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            /*Optional for more difficulty (it does increase the velocity of the ball when it bounces on a paddle*/
            ball.xVelocity++;
            if (ball.yVelocity > 0) {
                ball.yVelocity++;
            } else {
                ball.yVelocity--;
            }
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        if (ball.intersects(paddle2)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            /*Optional for more difficulty (it does increase the velocity of the ball when it bounces on a paddle*/
            ball.xVelocity++;
            if (ball.yVelocity > 0) {
                ball.yVelocity++;
            } else {
                ball.yVelocity--;
            }
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        /*This stops the paddles at windows edges */
        /*This is for the paddle1*/
        if (paddle1.y <= 0) {
            paddle1.y = 0;
        }
        if (paddle1.y >= (GAME_HEIGHT - PADDLE_HEIGHT)) {
            paddle1.y = GAME_HEIGHT - PADDLE_HEIGHT;
        }
        /*This is for the paddle2*/
        if (paddle2.y <= 0) {
            paddle2.y = 0;
        }
        if (paddle2.y >= (GAME_HEIGHT - PADDLE_HEIGHT)) {
            paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT;
        }

        /* Give a player one point and check for a winner */
        /* This is for player 2 */
        if (ball.x <= 0) {
            score.player2++;
            newPaddles();
            newBall();
        }
        /* This is for player 1 */
        if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
            score.player1++;
            newPaddles();
            newBall();
        }
    }

    /* This is a basic game loop*/
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                if (!gameOver) {
                    move();
                    checkCollision();
                }
                delta--;
            }

            if (!gameOver && (score.player1 >= MAX_SCORE || score.player2 >= MAX_SCORE)) {
                gameOver = true;
                finalScreen = true;
                repaint();
            }
            repaint();
        }
    }

    /*This is the code regarding the use of the keys to play */

    public class AL extends KeyAdapter {

        private boolean restarting = false;
        private boolean quitting = false;

        public void keyPressed(KeyEvent e) {
            if (!gameOver) {
                paddle1.keyPressed(e);
                paddle2.keyPressed(e);
            }

            if (finalScreen) {
                if (e.getKeyCode() == KeyEvent.VK_R && !restarting) {
                    // Reset the game
                    score.reset();
                    newPaddles();
                    newBall();
                    gameOver = false;
                    finalScreen = false;
                    restarting = true;
                } else if (e.getKeyCode() == KeyEvent.VK_Q && !quitting) {
                    // Quit the game
                    System.exit(0);
                }
            }
        }

        public void keyReleased(KeyEvent e) {
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);

            if (finalScreen) {
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    restarting = false;
                } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                    quitting = false;
                }
            }
        }
    }
}
