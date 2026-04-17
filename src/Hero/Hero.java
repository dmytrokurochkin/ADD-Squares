package Hero;

import javax.swing.*;
import java.awt.*;

public class Hero extends Entity {
    private int damage;
    private Image img = new ImageIcon("src/bulbasaur.png").getImage();

    public Hero() {
        super(10, 10, 100);
        this.damage = 10;
        initSize();
    }

    public Hero(int x, int y, int hp, int damage) {
        super(x, y, hp);
        this.damage = damage;
        initSize();
    }

    public Hero(int x, int y) {
        super(x, y, 100);
        this.damage = 10;
        initSize();
    }

    private void initSize() {
        this.width = 36;
        this.height = 36;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    public void setRawY(int y) {
        this.y = y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void draw(Graphics g) {
        if (img != null) {
            g.drawImage(img, x, y, width, height, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
        }
    }
}