package World;

import java.awt.*;

public class Block {
    private int x, y, size;

    private Color color;

    public Block(int x, int y, int size, Color color){
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }

    public int GetX() {return x;}
    public int GetY() {return y;}

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public void draw(Graphics g){
        g.setColor(color);
        g.fillRect(x, y, size, size);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, size, size);
    }
}
