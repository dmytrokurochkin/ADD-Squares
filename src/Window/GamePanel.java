package Window;

import Hero.Entity;
import Hero.Enemy;
import Hero.Hero;
import World.Block;
import World.GameMap;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GamePanel extends JPanel implements ActionListener, MouseMotionListener, MouseListener {
    private static final int MOVE_SPEED = 4;
    private static final double GRAVITY = 0.8;
    private static final double JUMP_VELOCITY = -12.0;
    private static final double MAX_FALL_SPEED = 14.0;

    private final Hero hero;
    private final List<Entity> entities;
    private final GameMap map;
    private final Timer timer;

    private Block hoveredBlock = null;
    private World.Map.Block selectedBlock = World.Map.Block.DIRT;

    private boolean leftPres = false;
    private boolean rightPres = false;
    private boolean jumpQueued = false;
    private double verticalVelocity = 0;

    private int cameraX = 0;
    private int cameraY = 0;

    private int worldPixelWidth = 8000;
    private int worldPixelHeight = 3200;

    public GamePanel(Hero hero) {
        this.hero = hero;

        int startW = 1280;
        int startH = 720;
        this.map = new GameMap(startW, startH);

        this.worldPixelWidth = Math.max(startW, 8000);
        this.worldPixelHeight = Math.max(startH, 3200);

        this.entities = new ArrayList<>();
        entities.add(hero);
        entities.add(new Enemy(200, 440));
        entities.add(new Enemy(600, 440));

        this.addMouseMotionListener(this);
        this.addMouseListener(this);

        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W) jumpQueued = true;
                if (e.getKeyCode() == KeyEvent.VK_A) leftPres = true;
                if (e.getKeyCode() == KeyEvent.VK_D) rightPres = true;

                if (e.getKeyCode() == KeyEvent.VK_1) selectedBlock = World.Map.Block.DIRT;
                if (e.getKeyCode() == KeyEvent.VK_2) selectedBlock = World.Map.Block.STONE;
                if (e.getKeyCode() == KeyEvent.VK_3) selectedBlock = World.Map.Block.WOOD;
                if (e.getKeyCode() == KeyEvent.VK_4) selectedBlock = World.Map.Block.LEAFS;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) leftPres = false;
                if (e.getKeyCode() == KeyEvent.VK_D) rightPres = false;
            }
        });

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int w = getWidth();
                int h = getHeight();
                if (w > 0 && h > 0) {
                    map.regenerate(w, h);

                    worldPixelWidth = Math.max(w, 8000);
                    worldPixelHeight = Math.max(h, 3200);

                    clampHeroToWorld();
                    clampCamera();
                    repaint();
                }
            }
        });

        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(new Color(135, 206, 235));

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(-cameraX, -cameraY);

        map.draw(g2d);

        if (hoveredBlock != null) {
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            Rectangle bounds = hoveredBlock.getBounds();
            g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }

        for (Entity entity : entities) {
            entity.draw(g2d);
        }

        g2d.dispose();

        g.setColor(new Color(0, 0, 0, 170));
        g.fillRoundRect(10, 10, 260, 30, 10, 10);
        g.setColor(Color.WHITE);
        g.drawString("Selected: " + selectedBlock.name() + "  [1-4]", 20, 30);

        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        applyGravity();
        clampHeroToWorld();
        updateCamera();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        updateHoveredBlock(e.getPoint());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        updateHoveredBlock(e.getPoint());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        requestFocusInWindow();

        int worldMouseX = e.getX() + cameraX;
        int worldMouseY = e.getY() + cameraY;

        if (SwingUtilities.isLeftMouseButton(e) && hoveredBlock != null) {
            map.removeBlock(hoveredBlock);
        }

        if (SwingUtilities.isRightMouseButton(e)) {
            map.placeBlock(worldMouseX, worldMouseY, selectedBlock, hero.getBounds());
        }

        updateHoveredBlock(e.getPoint());
        repaint();
    }

    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    private void updateHoveredBlock(Point mousePos) {
        Point worldPoint = new Point(mousePos.x + cameraX, mousePos.y + cameraY);
        hoveredBlock = map.getBlockAt(worldPoint);
    }

    private void updateCamera() {
        int panelW = Math.max(getWidth(), 1);
        int panelH = Math.max(getHeight(), 1);

        cameraX = hero.getX() + hero.getWidth() / 2 - panelW / 2;
        cameraY = hero.getY() + hero.getHeight() / 2 - panelH / 2;

        clampCamera();
    }

    private void clampCamera() {
        int panelW = Math.max(getWidth(), 1);
        int panelH = Math.max(getHeight(), 1);

        int maxCameraX = Math.max(0, worldPixelWidth - panelW);
        int maxCameraY = Math.max(0, worldPixelHeight - panelH);

        if (cameraX < 0) cameraX = 0;
        if (cameraY < 0) cameraY = 0;
        if (cameraX > maxCameraX) cameraX = maxCameraX;
        if (cameraY > maxCameraY) cameraY = maxCameraY;
    }

    private void clampHeroToWorld() {
        int maxX = worldPixelWidth - hero.getWidth();
        int maxY = worldPixelHeight - hero.getHeight();

        if (hero.getX() < 0) hero.setX(0);
        if (hero.getX() > maxX) hero.setX(maxX);

        if (hero.getY() < 0) {
            hero.setRawY(0);
            verticalVelocity = 0;
        }
        if (hero.getY() > maxY) {
            hero.setRawY(maxY);
            verticalVelocity = 0;
        }
    }

    private boolean isGrounded() {
        Rectangle feetBounds = new Rectangle(hero.getX(), hero.getY() + 1, hero.getWidth(), hero.getHeight());
        for (Block block : map.getBlocks()) {
            if (block.isSolid() && feetBounds.intersects(block.getBounds())) {
                return true;
            }
        }
        return false;
    }

    private void move() {
        int deltaX = 0;
        if (leftPres) deltaX -= MOVE_SPEED;
        if (rightPres) deltaX += MOVE_SPEED;

        if (deltaX != 0) {
            moveHorizontal(deltaX);
        }

        if (jumpQueued) {
            jump();
            jumpQueued = false;
        }
    }

    private void jump() {
        if (!isGrounded()) return;
        verticalVelocity = JUMP_VELOCITY;
    }

    private void applyGravity() {
        verticalVelocity = Math.min(verticalVelocity + GRAVITY, MAX_FALL_SPEED);
        moveVertical((int) Math.round(verticalVelocity));
    }

    private void moveHorizontal(int deltaX) {
        int direction = Integer.signum(deltaX);

        for (int moved = 0; moved < Math.abs(deltaX); moved++) {
            Rectangle nextBounds = new Rectangle(
                    hero.getX() + direction,
                    hero.getY(),
                    hero.getWidth(),
                    hero.getHeight()
            );

            Block collidingBlock = getCollidingSolidBlock(nextBounds);
            if (collidingBlock != null) return;

            hero.setX(hero.getX() + direction);
        }
    }

    private void moveVertical(int deltaY) {
        if (deltaY == 0) return;

        int direction = Integer.signum(deltaY);

        for (int moved = 0; moved < Math.abs(deltaY); moved++) {
            Rectangle nextBounds = new Rectangle(
                    hero.getX(),
                    hero.getY() + direction,
                    hero.getWidth(),
                    hero.getHeight()
            );

            Block collidingBlock = getCollidingSolidBlock(nextBounds);
            if (collidingBlock != null) {
                if (direction > 0) {
                    hero.setRawY(collidingBlock.GetY() - hero.getHeight());
                } else {
                    Rectangle blockBounds = collidingBlock.getBounds();
                    hero.setRawY(blockBounds.y + blockBounds.height);
                }
                verticalVelocity = 0;
                return;
            }

            hero.setRawY(hero.getY() + direction);
        }
    }

    private Block getCollidingSolidBlock(Rectangle bounds) {
        for (Block block : map.getBlocks()) {
            if (block.isSolid() && bounds.intersects(block.getBounds())) {
                return block;
            }
        }
        return null;
    }
}