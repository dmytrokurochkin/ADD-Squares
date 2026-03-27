package World;

import Window.MyWindow;
import java.awt.Graphics;
import java.util.ArrayList;

public class GameMap {
    private ArrayList<Block> blocks;
    public static final int BLOCK_SIZE = 40;

    public GameMap() {
        blocks = new ArrayList<>();
        generateWorld();
    }

    public void generateWorld(){
        int windowWidth = MyWindow.getWindowWidth();
        int windowHeight = MyWindow.getWindowHeight();

        int cols = windowWidth / BLOCK_SIZE;
        int rows = windowHeight / BLOCK_SIZE;

        for(int row = 0; row < rows; row++){
            for(int col = 0; col < cols; col++) {
                int x = col * BLOCK_SIZE;
                int y = row * BLOCK_SIZE;

                if (row < 8) {
                    continue;
                } else if (row == 8) {
                    blocks.add(new Block(x, y, BLOCK_SIZE, Map.Block.GRASS));
                } else if (row == 9 || row == 10) {
                    blocks.add(new Block(x, y, BLOCK_SIZE, Map.Block.DIRT));
                } else {
                    blocks.add(new Block(x, y, BLOCK_SIZE, Map.Block.STONE));
                }
            }

            }
        }

        public ArrayList<Block> getBlocks() {
            return blocks;
        }

        public void draw(Graphics g){
            for(Block block : blocks){
                block.draw(g);
            }
        }
    }



