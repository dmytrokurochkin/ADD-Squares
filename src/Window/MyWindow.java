package Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MyWindow extends JFrame {

    private static int windowWidth;
    private static int windowHeight;

    private boolean fullscreen = false;
    private Rectangle windowedBounds = new Rectangle(100, 100, 1280, 720);

    public static int getWindowWidth() {
        return windowWidth;
    }

    public static int getWindowHeight() {
        return windowHeight;
    }

    public MyWindow(JPanel gamePanel) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int startW = Math.max(960, (int) (screenSize.width * 0.8));
        int startH = Math.max(600, (int) (screenSize.height * 0.8));

        windowWidth = startW;
        windowHeight = startH;
        windowedBounds = new Rectangle(100, 100, startW, startH);

        setTitle("ADD-Squares");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(960, 600));
        setBounds(windowedBounds);
        setLocationRelativeTo(null);

        add(gamePanel);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                windowWidth = getContentPane().getWidth();
                windowHeight = getContentPane().getHeight();
            }

            @Override
            public void componentMoved(java.awt.event.ComponentEvent e) {
                if (!fullscreen) {
                    windowedBounds = getBounds();
                }
            }
        });

        bindFullscreenToggle();

        setVisible(true);

        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
    }

    private void bindFullscreenToggle() {
        JRootPane root = getRootPane();
        InputMap inputMap = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = root.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("F11"), "toggleFullscreen");
        actionMap.put("toggleFullscreen", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleFullscreen();
            }
        });
    }

    private void toggleFullscreen() {
        GraphicsConfiguration gc = getGraphicsConfiguration();
        Rectangle screenBounds = gc.getBounds();

        SwingUtilities.invokeLater(() -> {
            if (!fullscreen) {
                windowedBounds = getBounds();

                dispose();
                setUndecorated(true);
                setResizable(false);
                setBounds(screenBounds);
                setVisible(true);

                fullscreen = true;
            } else {
                dispose();
                setUndecorated(false);
                setResizable(true);
                setBounds(windowedBounds);
                setLocationRelativeTo(null);
                setVisible(true);

                fullscreen = false;
            }

            windowWidth = getContentPane().getWidth();
            windowHeight = getContentPane().getHeight();

            if (getContentPane().getComponentCount() > 0) {
                Component c = getContentPane().getComponent(0);
                c.setFocusable(true);
                c.requestFocusInWindow();
            }
        });
    }
}