package Hero;

import java.awt.Color;
import java.awt.Graphics;

public class Enemy extends Entity {
    private Color color;

    public Enemy(int x, int y) {
        super(x, y, 50);
        this.color = Color.MAGENTA;
        this.width = 30;
        this.height = 30;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, width, height);
    }
}
