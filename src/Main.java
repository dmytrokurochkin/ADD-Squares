import Hero.Hero;
import Window.GamePanel;
import Window.MyWindow;

public class Main {
    public static void main(String[] args) {
        Hero bulbasaur = new Hero(10, 10, 100, 10);
        GamePanel Panel = new GamePanel(bulbasaur);
        new MyWindow(800, 600, Panel);
    }
}
