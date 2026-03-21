package Window;

import Hero.Hero;
import World.Block;
import World.GameMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements ActionListener {
    private Hero hero;
    private GameMap map;
    private Timer timer;

    public GamePanel(Hero hero){
        this.hero = hero;
        this.map = new GameMap();

        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        this.setBackground(new Color(135, 206, 235));

        map.draw(g);

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
        applyGravityAndCollision();
        repaint();
    }

    private void applyGravityAndCollision() {
        int gravitySpeed = 5;

        Rectangle nextPosition = new Rectangle(hero.getX(), hero.getY() + gravitySpeed, hero.getWidth(), hero.getHeight());

        boolean isGrounded = false;

        for (Block block : map.getBlocks()) {
            if (nextPosition.intersects(block.getBounds())) {
                isGrounded = true;
                hero.setRawY(block.GetY() - hero.getHeight());
                break;
            }
        }

        if (!isGrounded) {
            hero.setRawY(hero.getY() + gravitySpeed);
        }
    }
}

