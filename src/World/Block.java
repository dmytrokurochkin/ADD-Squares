package World;

import java.awt.Rectangle;
import java.awt.Graphics;

public class Block implements Collidable {
    private int x, y, size;
    private final Map.Block type;

    public Block(int x, int y, int size, Map.Block type) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.type = type;
    }

    public int GetX() {
        return x;
    }

    public int GetY() {
        return y;
    }

    public Map.Block getType() {
        return type;
    }

    @Override
    public boolean isSolid() {
        return type.isSolid();
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public void draw(Graphics g) {
        g.setColor(type.getBlockColor());
        g.fillRect(x, y, size, size);
    }
}
