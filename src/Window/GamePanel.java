package Window;

import Hero.Hero;
import World.Block;
import World.GameMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class GamePanel extends JPanel implements ActionListener, MouseMotionListener {
    private Hero hero;
    private GameMap map;
    private Timer timer;
    private Block hoveredBlock = null;
    //movement
    public static int gravitySpeed = 5;
    private boolean leftPres = false;
    private boolean rightPres = false;
    private boolean jumpPres = false;

    public GamePanel(Hero hero){
        this.hero = hero;
        this.map = new GameMap();

        timer = new Timer(16, this);
        timer.start();

        this.addMouseMotionListener(this);
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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

    private void updateHoveredBlock(Point mousePos){
        hoveredBlock = null;
        for(Block block : map.getBlocks()){
            if(block.getBounds().contains(mousePos)){
                hoveredBlock = block;
                break;
            }
        }
    }

    private boolean isGrounded() {
        Rectangle nextPosition = new Rectangle(hero.getX(), hero.getY() + gravitySpeed, hero.getWidth(), hero.getHeight());

        for (Block block : map.getBlocks()) {
            if (nextPosition.intersects(block.getBounds())) {
                return true;
            }
        }

        return false;
    }

    private void applyGravity() {
        if (!isGrounded()) {
            hero.setRawY(hero.getY() + gravitySpeed);
            return;
        }

        Rectangle nextPosition = new Rectangle(hero.getX(), hero.getY() + gravitySpeed, hero.getWidth(), hero.getHeight());
        for (Block block : map.getBlocks()) {
            if (nextPosition.intersects(block.getBounds())) {
                hero.setRawY(block.GetY() - hero.getHeight());
                return;
            }
        }
    }
}

