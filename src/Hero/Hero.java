package Hero;

import Window.MyWindow;

import javax.swing.*;
import java.awt.*;

public class Hero {
    int x, y, hp, damage;
    private int width = 36, height = 36;
    Image img = new ImageIcon("C:\\Users\\Kira\\Documents\\ADD-Squares\\src\\bulbasaur.png").getImage() ;
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
    public int getDamage() {return damage;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}

    public Image getImg() {
        return img;
    }

    public void setX(int x) {
        this.x = x % MyWindow.getWindowWidth();
        if(this.x < 0) this.x = MyWindow.getWindowWidth() + this.x;
    }

    public void setRawY(int y){
        this.y = y;
    }

//    public void setY(int y) {
//        this.y = y % MyWindow.getWindowWidth();
//        if(this.y < 0)
//            this.y = MyWindow.getWindowWidth() + this.y;
//    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
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