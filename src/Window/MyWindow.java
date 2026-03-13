package Window;

import javax.swing.*;

public class MyWindow extends JFrame {
    private static int windowWidth, windowHeight;
    public static int getWindowWidth() {
        return windowWidth;
    }
    public static int getWindowHeight() {
        return windowHeight;
    }
    public MyWindow(int w, int h) {
        this.setSize(w, h);
        windowHeight = h;
        windowWidth = w;
        this.setVisible(true);
    }
    public MyWindow() {
        this.setVisible(true);
        this.setSize(200, 200);
    }
}
