package Window;

import javax.swing.*;

public class MyWindow extends JFrame {

    private static int windowWidth = 1920, windowHeight = 1200;

    public static int getWindowWidth() {
        return windowWidth;
    }
    public static int getWindowHeight() {
        return windowHeight;
    }

    public MyWindow(int w, int h, JPanel gamePanel) {

        windowHeight = h;
        windowWidth = w;

        this.setTitle("ADD-Squares");
        this.setSize(w, h);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setResizable(false);
        this.setLocationRelativeTo(null);

        this.add(gamePanel);

        this.setVisible(true);
    }

//    public MyWindow() {
//        this.setVisible(true);
//        this.setSize(200, 200);
//    }
}
