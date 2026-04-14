package Hero;

import World.Collidable;
import java.awt.Graphics;
import java.awt.Rectangle;

//abstract entity
public abstract class Entity implements Collidable {
    protected int x, y, hp;
    protected int width, height;

    public Entity() {
        this(0, 0, 100);
    }

    public Entity(int x, int y, int hp) {
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.width = 40;
        this.height = 40;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    public abstract void draw(Graphics g);

    public void heal() {
        this.hp += 10;
    }

    public void heal(int amount) {
        this.hp += amount;
    }
}