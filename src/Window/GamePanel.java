package Window;

import Hero.Hero;
import World.Block;
import World.GameMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GamePanel extends JPanel implements ActionListener, MouseMotionListener, MouseListener {
    private Hero hero;
    private GameMap map;
    private Timer timer;
    private Block hoveredBlock = null;
    //movement
    public static int mvSpeed = 5;
    public static int gravitySpeed = 7;
    private boolean leftPres = false;
    private boolean rightPres = false;
    private boolean jumpPres = false;
    private boolean jumpInProgress = false;

    public GamePanel(Hero hero){
        this.hero = hero;
        this.map = new GameMap();

        this.addMouseMotionListener(this);
        this.addMouseListener(this);

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W) jumpPres = true;
                if (e.getKeyCode() == KeyEvent.VK_A) leftPres = true;
                if (e.getKeyCode() == KeyEvent.VK_D) rightPres = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W) jumpPres = false;
                if (e.getKeyCode() == KeyEvent.VK_A) leftPres = false;
                if (e.getKeyCode() == KeyEvent.VK_D) rightPres = false;
            }
        });

        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        this.setBackground(new Color(135, 206, 235));

        map.draw(g);

        if(hoveredBlock != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            Rectangle bounds = hoveredBlock.getBounds();
            g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
            g2d.dispose();
        }

        Image img = hero.getImg();
        if(img != null) {
            g.drawImage(img, hero.getX(), hero.getY(), hero.getWidth(), hero.getHeight(), null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(hero.getX(), hero.getY(), hero.getWidth(), hero.getHeight());
        }

        Toolkit.getDefaultToolkit().sync();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        applyGravity();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e){
        updateHoveredBlock(e.getPoint());
    }

    @Override
    public void mouseDragged(MouseEvent e){
        updateHoveredBlock(e.getPoint());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        requestFocusInWindow();

        if (SwingUtilities.isLeftMouseButton(e) && hoveredBlock != null) {
            map.removeBlock(hoveredBlock);
        }

        if (SwingUtilities.isRightMouseButton(e)) {
            map.placeBlock(e.getX(), e.getY(), Map.Block.DIRT, hero.getBounds());
        }

        updateHoveredBlock(e.getPoint());
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private void updateHoveredBlock(Point mousePos){
        hoveredBlock = map.getBlockAt(mousePos);
    }

    private boolean isGrounded() {
        Rectangle nextPosition = new Rectangle(hero.getX(), hero.getY() + gravitySpeed, hero.getWidth(), hero.getHeight());

        for (Block block : map.getBlocks()) {
            if (block.isSolid() && nextPosition.intersects(block.getBounds())) {
                return true;
            }
        }

        return false;
    }

    private void move() {
            if (leftPres) hero.setX(hero.getX() - mvSpeed);
            if (rightPres) hero.setX(hero.getX() + mvSpeed);
            if (jumpPres) jump();

    }

    private void jump() {
        if (jumpInProgress || !isGrounded()) return;

        int jumpSpeedAcc = 3;

        jumpInProgress = true;
        gravitySpeed = -gravitySpeed - jumpSpeedAcc;

        Timer jumpTimer = new Timer(250, e -> {
            gravitySpeed = -gravitySpeed - jumpSpeedAcc;
            jumpInProgress = false;
        });
        jumpTimer.setRepeats(false);
        jumpTimer.start();
    }

    private void applyGravity() {
        if (!isGrounded()) {
            hero.setRawY(hero.getY() + gravitySpeed);
            return;
        }

        Rectangle nextPosition = new Rectangle(hero.getX(), hero.getY() + gravitySpeed, hero.getWidth(), hero.getHeight());
        for (Block block : map.getBlocks()) {
            if (block.isSolid() && nextPosition.intersects(block.getBounds())) {
                hero.setRawY(block.GetY() - hero.getHeight());
                return;
            }
        }
    }
}
