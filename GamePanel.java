package Snake;

import javax.swing.*;

public class GamePanel{
    public static void main(String[] args) throws Exception {
        int boardWidth = 600;
        int boardHeight = boardWidth;

        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null); //ni buat tentukan nnti pas run panel ny mw dimana lokasinya, tp krn null maka dia muncul di tengah" layar
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //supaya klo bisa tutup ketika w lang tekan tombol x samping kanan

        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
        frame.add(snakeGame); //supaya bisa implementasi code dr snakegame ke frame ny
        frame.pack(); //supaya sesuai ukuran, karena klo g pake ni itu ukuran 600 kali 600 termasuk kotak title ny
        snakeGame.requestFocus();//ni buat biar si snake game ny ikutin ("denger") perintah dr keyboard
    }
}
