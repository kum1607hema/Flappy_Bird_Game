import javax.swing.*;
public class AppB {
    public static void main(String[] args) throws Exception {
        int boardWidth = 360;
        int boardHeight =640;

        JFrame frame = new JFrame("Flappy Birds");
        // frame.setVisible(true);
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null); // should window at center
        frame.setResizable(false); // window should not be resizeable
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        // cretae an instance of FlappyBird
        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack();           // use to exclude title bar from dimension of frame
        flappyBird.requestFocus();
        frame.setVisible(true);

    }
}
