package brickbreaker_intellij;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

public class GamePlay extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;
    private int score = 0;

    private int totalBricks = 21;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;

    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballXDir = -1;
    private int ballYDir = -2;

    private MapGenerator map;

    public GamePlay(){
        map = new MapGenerator(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false); // dont know what it does
        timer = new Timer(delay, this);
        timer.start();
    }

    /**
     *
     * @param g
     */
    @Override
    public void paint(Graphics g){
        //background
        g.setColor(Color.BLACK);
        g.fillRect(1, 1, 692, 592);

        //drawing map
        map.draw((Graphics2D)g);

        //border
        g.setColor(Color.yellow);
        g.fillRect(0,0,3,592);
        g.fillRect(0,0,692,3);
        g.fillRect(691,0,3,592);

        //scores
        g.setColor(Color.white);
        g.setFont(new Font("Serif", Font.BOLD, 25));
        g.drawString(""+score,590,30);

        //the paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        //ball
        g.setColor(Color.yellow);
        g.fillOval(ballPosX, ballPosY, 20,20);

        if(totalBricks <= 0){
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            g.setColor(Color.RED);

            g.setFont(new Font("Serif", Font.BOLD, 30));
            g.drawString("YOU WON,  score: "+score,260,300);

            g.setFont(new Font("Serif", Font.BOLD, 20));
            g.drawString("Press ENTER TO restart",250,350);
        }

        if(ballPosY > 570){
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            g.setColor(Color.RED);

            g.setFont(new Font("Serif", Font.BOLD, 30));
            g.drawString("GAME OVER,  score: "+score,190,300);

            g.setFont(new Font("Serif", Font.BOLD, 20));
            g.drawString("Press ENTER TO restart",250,350);

        }

        g.dispose(); // ????
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        timer.start(); // dont know what it does

        if(play){  /* how the coordinates are working */

            if(new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))){
                ballYDir = -ballYDir;
            }

            A: for(int i=0; i < map.map.length; i++){
                for(int j=0; j < map.map[0].length; j++){
                    if(map.map[i][j] > 0){
                        int brickX = j*map.brickWidth + 80;
                        int brickY = j*map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY,brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect)){
                            map.setBrickValue(0,i,j);
                            totalBricks--;
                            score += 5;

                            if(ballPosX+19 <= brickRect.x || ballPosX+1 >= brickRect.x+brickRect.width){
                                ballXDir = -ballXDir;
                            }
                            else{
                                ballYDir = -ballYDir;
                            }

                            break A;
                        }

                    }
                }
            }

            ballPosX += ballXDir;
            ballPosY += ballYDir;

            if(ballPosX < 0){
                ballXDir = -ballXDir;
            }
            if(ballPosY < 0){
                ballYDir = -ballYDir;
            }
            if(ballPosX > 670){
                ballXDir = -ballXDir;
            }
        }

        repaint(); // dont know what it does
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) { /*we dont need this*/}
    @Override
    public void keyReleased(KeyEvent keyEvent) { /*we dont need this*/}

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode() == KeyEvent.VK_RIGHT){
            if(playerX >= 600){
                playerX = 600;
            }
            else{
                moveRight();
            }
        }
        if(keyEvent.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX < 10){
                playerX = 10;
            }
            else{
                moveLeft();
            }
        }

        if(keyEvent.getKeyCode() == keyEvent.VK_ENTER){
            if(!play){
                play = true;
                ballPosX = 120;
                ballPosY = 350;
                ballXDir = -1;
                ballYDir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3,7);

                repaint();
            }
        }
    }

    public void moveRight(){
        play = true;
        playerX+=20;
    }
    public void moveLeft(){
        play = true;
        playerX-=20;
    }

}
