package Snake;

import java.awt.*; //ni si biar kita bisa gunakan si Graphics utk atur warna font
import java.awt.event.*; //klo ni biar kita bisa pake action listener sama key listener, supaya pas kita main tuh bisa pake keyboard
import java.util.ArrayList; //storing the segments of the snake's body
import java.util.Random; //getting random x and y values to place the food on the screen
import javax.swing.*; //ni si buat kita bisa menggunakan JPanel ama JFrame, soalny klo g import g bakal bisa kita pake

public class SnakeGame extends JPanel implements ActionListener, KeyListener{
    private class Tile { //private supaya hny SnakeGame yang bisa akses class nihh
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    int boardWidth;
    int boardHeight;    
    int tileSize = 25;

    //Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //Food
    Tile food;
    Random random;

    //game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameStarted = false;
    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this); //nih biar kita bisa menggunakan keyboard sbg perintah biar si ular bisa gerak
        setFocusable(true); // nih biar dia bisa paham, bisa jalankan perintah

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile (10, 10);
        random = new Random();
        placeFood();

        gameLoop = new Timer(100, this);
        gameLoop.start();
        //100 adalah interval waktu dalam milidetik (0,1 detik), artinya setiap 0,1 detik, game akan:
        //Menggerakkan ular.
        //Memeriksa kondisi (apakah makanan dimakan, tabrakan terjadi, dll).
        //Menggambar ulang layar (repaint).

        velocityX = 0;//ni artinya pas awal mulai game, kecepatannya 0 (g bergerak)
        velocityY = 0;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //Grid
//        for (int i = 0; i < boardWidth/tileSize; i++){
//            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
//            g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
//        }
        if (!gameStarted) {
            // Tampilkan pesan awal sebelum game dimulai
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Press 'Space Bar' to Start", 150, 280);
            return; // Keluar dari metode draw supaya snake dan food tidak digambar
        }
        if (!gameOver) {
            //Snake Head
            g.setColor(Color.green);
            //g.fillRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize);
            g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

            //Snake Body
            for (int i = 0; i < snakeBody.size(); i++) {
                Tile snakePart = snakeBody.get(i);
                //g.fillRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize);
                g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
            }//apparently klo lu e snake body susunannya snake head lalu food lalu snake body, warna bodyny jd merah TT


            //Food
            g.setColor(Color.red);
            //g.fillRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize);
            g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);
        }

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("GAME OVER!", tileSize + 215, tileSize + 230);
            g.setColor(Color.white);
            g.drawString("Your Score: " + String.valueOf(snakeBody.size()), tileSize + 220, tileSize + 250);
            g.drawString("Press 'Enter' to Restart Game", tileSize + 165, tileSize + 270);
        }
        else {
            g.setColor(Color.green);
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }

    public void placeFood() {
        food.x = random.nextInt(boardWidth/tileSize); //jd food ny tuh bakal di posisi yg random antara 0-24 krn 600/25 = 24
        food.y = random.nextInt(boardHeight/tileSize); //tiap kali run bakal beda" posisi food ny
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y; //utk cek apakah 2 tile (snake) tabrak ato g
    }

    public void move() {
        //eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        //snake body (nihh supaya pas dia makan nnti body ny bakal nyambung ke head, bukan ttp di posisi food sblmnya en cuma modal ganti warna)
        for (int i = snakeBody.size()-1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i==0) { //nih (utk tile pertama setelah kepala) biar jika dia kek member pertama body ny nnti dia bakal asik copas posisi head ny gitu biar terkesan ngikut kepala
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else{
                Tile prevSnakePart = snakeBody.get(i-1); //utk tile selanjutnya, mulai dr ke 2 stlh kepala
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //Snake Head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game over conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            //collide  with the snake head
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth ||
            snakeHead.y * tileSize < 0 || snakeHead.y * tileSize > boardHeight) {
            gameOver = true;
        }
    }

    public void resetGame() {
        // Set ulang posisi awal snake dan makanan
        snakeHead = new Tile(5, 5);
        snakeBody.clear();  // Hapus semua segmen tubuh snake
        placeFood();        // Tempatkan makanan di posisi baru

        // Set ulang arah dan status game
        velocityX = 0;
        velocityY = 0;
        gameOver = false;

        // Mulai ulang game loop
        gameLoop.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();//ni buat setiap 100 milisecond sesuai timer, nnti bakal asik repaint (draw ulang"), kecuali dia gameover
        }
    }

    //sedangkan klo nih butuh krn w lang hny butuh interaksi dari panah di keyboard buat main game nihh
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY !=1) {
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY !=-1) {
            velocityX = 0;
            velocityY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX !=1) {
            velocityX = -1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX !=-1) {
            velocityX = 1;
            velocityY = 0;
        }
        //&& nih semua fungsinya supaya klo dia udh ke kiri dia g bisa balek ke arah kanan gitu di row yg sama hrs naik ato turun dlu br bisa balek arah kiri/kanan

        else if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameStarted) {
            gameStarted = true;  // Game dimulai
            gameLoop.start();    // Timer dimulai
            repaint();           // Menggambarkan ulang agar snake dan food muncul
        }

        else if (e.getKeyCode() == KeyEvent.VK_ENTER && gameOver) {
            resetGame();  // Panggil reset game saat space bar ditekan
        }
    }

    //nih kedua g perlu si, hny buat mendefine fungsi key listener supaya bisa jalan ja
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
