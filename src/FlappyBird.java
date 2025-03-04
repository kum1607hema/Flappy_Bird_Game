import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;  //this will store all pipes 
import java.util.Random;     //used for placing pipe at random positiom
import javax.swing.*;


public class FlappyBird extends JPanel implements ActionListener, KeyListener {   // help to inherit all feature of JPanel
    int boardWidth = 360;
    int boardHeight = 640;
    
    // Image
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg; 
    Image bottomPipeImg;

     //Bird 
     int birdX = boardWidth/8;
     int birdY = boardHeight/2;
     int birdWidth = 34;
     int birdHeight = 24;

     class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;
        
        Bird(Image img){
            this.img= img;
        }

     }
     //Pipes
     int pipeX = boardWidth;
     int pipeY = 0;
     int pipeWidth = 64;        //scaled by 1/6
     int pipeHeight = 512;      

     class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passes = false;

        Pipe(Image img){
            this.img = img;
        }
     }



     // game logic
     Bird bird;
     int velocityX = -4; // move pipe to the left speed (simulates bird moving right)
     int velocityY = -9; // move bird up/down speed
     int gravity = 1;

     ArrayList<Pipe> pipes;
     Random random = new Random();

     Timer gameLoop;
     Timer placePipesTimer;
     boolean gameOver = false ;
     double score = 0;

    FlappyBird(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        // setBackground(Color.BLUE);
        setFocusable(true);
        addKeyListener(this);

        //load image
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        //bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        //place pipes timer
        placePipesTimer = new Timer(1500, new ActionListener(){   //1000 = 1 sec thus 15000 = 1.5 sec
            @ Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });

        placePipesTimer.start();

       //game timer
        gameLoop = new Timer(1000/60, this );  // 1000/16 = 16.6
        gameLoop.start();

    }

    public void placePipes(){
        //(0-1)* pipeHeight/2 -> (0-256)
        //128
        //0-120 -(0-256) --> 1/4 pipeHeight/4 -> 3/4 pipeHeight   this gona how much we shouuld take the y position

        int randomPipeY = (int)(pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/2;

        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }
    
    public void paintComponent(Graphics g){
         super.paintComponent(g);
        draw(g);
    } 
    
    public void draw(Graphics g){
        //background
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        //bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        //pipes
        for(int i=0; i<pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        //score 
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if(gameOver){
            g.drawString("Game Over "+ String.valueOf((int) score), 10 , 35);   //10 right 7 35 pixel down
        }
        else {
            g.drawString( String.valueOf((int) score), 10 , 35);
        }
    }

    public void move(){
        //bird
        velocityY += gravity;
        bird.y += velocityY;  // move upward -6 pixel at the rate of per frame
        bird.y = Math.max(bird.y, 0);

        //pipe
        for(int i=0; i<pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if(!pipe.passes && bird.x > pipe.x + pipe.width ){
                pipe.passes = true; 
                score += 0.5; // 0.5 because there are 2 pipes! so 0,5*2 =1, 1 for each set of pipes
            }

            if(collision(bird, pipe)){    // get collied with the pipe game get over
                gameOver = true;
            }
        }

        if(bird.y> boardHeight){
            gameOver = true;
        }
    }

    public boolean collision(Bird a, Pipe b){
        return a.x < b.x + b.width &&            //a's top left corner doesn't reach b's top right corner
                a.x + a.width >b.x &&            //a's top right corner passes b's top left corner
                a.y <b.y + b.height &&          //a's top left corner doesn't reach b's bottom left corner
                a.y + a.height > b.y;            // a's bottom left corner passes b's top left corner
    
        }
    @Override
    public void actionPerformed(ActionEvent e) {
        //paint component
        move();  // 60 times per sec
        repaint();
        if(gameOver){
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }

    

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY =-9;
        }
        
        if(gameOver){
            //restart the game by resetting the condiction
            bird.y = birdY;
            velocityY = 0;
            pipes.clear();
            score = 0;
            gameOver = false;
            gameLoop.start();
            placePipesTimer.start();

        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}