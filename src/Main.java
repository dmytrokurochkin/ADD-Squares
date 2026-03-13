import Hero.Hero;
import Window.MyWindow;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Hero> Heroes = new ArrayList<>();
        Hero bulbasaur = new Hero(10, 10, 100, 10);
        Heroes.add(bulbasaur);
        new MyWindow(600, 600);
    }
}
