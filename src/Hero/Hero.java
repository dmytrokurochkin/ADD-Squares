package Hero;

import Window.MyWindow;

import javax.swing.*;
import java.awt.*;

public class Hero {
    int x, y, hp, damage;
    Image img = new ImageIcon("a.png").getImage() ;
    public Hero(int x, int y, int hp, int damage) {
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.damage = damage;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getHp() {
        return hp;
    }
    public int getDamage() {
        return damage;
    }
    public Image getImg() {
        return img;
    }

    public void setX(int x) {
        this.x = x % MyWindow.getWindowWidth();
        if(this.x < 0)
            this.x = MyWindow.getWindowWidth() + this.x;
    }

    public void setY(int y) {
        this.y = y % MyWindow.getWindowWidth();
        if(this.y < 0)
            this.y = MyWindow.getWindowWidth() + this.y;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setImg(Image img) {
        this.img = img;
    }
}