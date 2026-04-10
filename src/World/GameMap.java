package World;

import Window.MyWindow;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.util.ArrayList;

public class GameMap {
    private final ArrayList<Block> blocks;
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

                if (row < 12) {
                    continue;
                } else if (row == 12) {
                    blocks.add(new Block(x, y, BLOCK_SIZE, Map.Block.GRASS));
                } else if (row == 13 || row == 14) {
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

    public Block getBlockAt(Point point) {
        for (Block block : blocks) {
            if (block.getBounds().contains(point)) {
                return block;
            }
        }

        return null;
    }

    public boolean removeBlock(Block block) {
        return blocks.remove(block);
    }

    public boolean placeBlock(int pixelX, int pixelY, Map.Block type, Rectangle forbiddenArea) {
        int snappedX = (pixelX / BLOCK_SIZE) * BLOCK_SIZE;
        int snappedY = (pixelY / BLOCK_SIZE) * BLOCK_SIZE;

        if (snappedX < 0 || snappedY < 0) return false;
        if (snappedX >= MyWindow.getWindowWidth() || snappedY >= MyWindow.getWindowHeight()) return false;

        Rectangle newBlockBounds = new Rectangle(snappedX, snappedY, BLOCK_SIZE, BLOCK_SIZE);
        if (forbiddenArea != null && newBlockBounds.intersects(forbiddenArea)) {
            return false;
        }

        for (Block block : blocks) {
            if (block.getBounds().intersects(newBlockBounds)) {
                return false;
            }
        }

        blocks.add(new Block(snappedX, snappedY, BLOCK_SIZE, type));
        return true;
    }

    public void draw(Graphics g){
        for(Block block : blocks){
            block.draw(g);
        }
    }
}

