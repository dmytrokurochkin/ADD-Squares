package World;

import java.awt.*;

public class Block {
    private int x, y, size;
    private final Map.Block type;

    public Block(int x, int y, int size, Map.Block type){
        this.x = x;
        this.y = y;
        this.size = size;
        this.type = type;
    }

    public int GetX() {return x;}
    public int GetY() {return y;}
    public Map.Block getType() {return type;}
    public boolean isSolid() {return type.isSolid();}

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public void draw(Graphics g){
        g.setColor(type.getBlockColor());
        g.fillRect(x, y, size, size);

//        block border creating, comment next 2 lines to see complete non-bordered world
//        g.setColor(Color.BLACK);
//        g.drawRect(x, y, size, size);
    }
}
