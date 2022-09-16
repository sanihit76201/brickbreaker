package brickbreaker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.*;
import javax.swing.Timer;
import javax.swing.JPanel;

public class GamePlay extends JPanel implements KeyListener,ActionListener{

    private boolean play=false;
    private int score=0;
    private int totalBricks=21; 
    private Timer timer;
    private int delay=10;

    private int playerX=210;// starting position of the sloder
    private int ballposX=150;//starting ball position
    private int ballposY=350;
    private int ballXdir=-1;//direction of the ball
    private int ballYdir=-2;

    private MapGenerator map;
    public GamePlay() {
        map=new MapGenerator(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer=new Timer(delay,this);
        timer.start();
    }
    public void paint(Graphics g) {
        //background
         g.setColor(Color.pink);
         g.fillRect(1,1,692,592);
         //drawing map
         map.draw((Graphics2D)g);
         //borders
         g.setColor(Color.yellow);
         g.fillRect(0,0,3,592);
         g.fillRect(0,0,692,3);
         g.fillRect(691,0,3,592);
         //scores
         g.setColor(Color.blue);
         g.setFont(new Font("serif",Font.BOLD,20));
         g.drawString("Your Score :"+score,500,30);
         //the paddle
         g.setColor(Color.black);
         g.fillRect(playerX,550,100,10);
         //the ball
         g.setColor(Color.RED);
         g.fillOval(ballposX,ballposY,20,20);

         if(totalBricks<=0) {
            play=false;
            ballXdir=0;
            ballYdir=0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("WELL PLAYED! YOU WON!-",150,300);
            g.setFont(new Font("serif",Font.BOLD,20));
            g.drawString("Press Enter to Restart ",230,350);
         }
         if(ballposY >570) {
            play=false;
            ballXdir=0;
            ballYdir=0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("WELL PLAYED! GAME OVER ",100,300);
            g.setFont(new Font("serif",Font.BOLD,20));
            g.drawString("Press Enter to Restart ",230,350);
        }
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(play){//if the player is true then check the borders and directions
            if(new Rectangle(ballposX,ballposY,20,20).intersects(new Rectangle(playerX,550,100,8))) {
                ballYdir=-ballYdir;
            }
            A:for(int i=0;i<map.map.length;i++) {
                for(int j=0;j<map.map[0].length;j++) {
                    if(map.map[i][j]>0) {
                        int brickX=j*map.brickWidth+80;
                        int brickY=i*map.brickHeight+50;
                        int brickWidth=map.brickWidth;
                        int brickHeight=map.brickHeight;

                        Rectangle rect=new Rectangle(brickX,brickY,brickWidth,brickHeight);
                        Rectangle ballRect=new Rectangle(ballposX,ballposY,20,20);
                        Rectangle brickRect=rect;
                        if(ballRect.intersects(brickRect)) {
                            map.setBrickValue(0,i,j);
                            totalBricks--;
                            score+=5;

                            if(ballposX+19<-brickRect.x || ballposX+1>=brickRect.x+brickRect.width) {
                                ballXdir=-ballXdir;
                            }
                            else {
                                ballYdir=-ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }

            ballposX+=ballXdir;
            ballposY+=ballYdir;
            if(ballposX< 0) {
                ballXdir=-ballXdir;
            }
            if(ballposY< 0) {
                ballYdir=-ballYdir;
            }
            if(ballposX> 670) {
                ballXdir=-ballXdir;
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_RIGHT) {//right arrow moves the slider to right
            if(playerX +20>=600) {
                playerX=600;
            }
            else {
                moveRight();
            }
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT) {//left arrow moves the slider to left
            if(playerX < 10) {
                playerX=10;
            }
            else {
                moveLeft();
            }
        }
        if(e.getKeyCode()==KeyEvent.VK_ENTER) {// if we press enter
            if(!play) {
                play=true;
                ballposX=120;
                ballposY=350;
                ballXdir=-1;
                ballYdir=-2;
                playerX=310;
                score=0;
                totalBricks=21;
                map=new MapGenerator(3,7);

                repaint();
            }
        }
    }
    public void moveRight() {
        play=true;
        playerX+=20;
    }
    public void moveLeft() {
        play=true;
        playerX-=20;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}