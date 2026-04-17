package World;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class GameMap {
    private final ArrayList<Block> blocks;
    public static final int BLOCK_SIZE = 40;

    private static final int MIN_WORLD_WIDTH = 8000;
    private static final int MIN_WORLD_HEIGHT = 3200;

    private int worldPixelWidth;
    private int worldPixelHeight;

    private long seed = 123456789L;
    private Random random;

    public GameMap(int worldPixelWidth, int worldPixelHeight) {
        blocks = new ArrayList<>();
        regenerate(worldPixelWidth, worldPixelHeight);
    }

    public void regenerate(int worldPixelWidth, int worldPixelHeight) {
        this.worldPixelWidth = Math.max(worldPixelWidth, MIN_WORLD_WIDTH);
        this.worldPixelHeight = Math.max(worldPixelHeight, MIN_WORLD_HEIGHT);

        blocks.clear();
        random = new Random(seed);
        generateWorld();
    }

    private void generateWorld() {
        int cols = worldPixelWidth / BLOCK_SIZE;
        int rows = worldPixelHeight / BLOCK_SIZE;

        int[] surface = new int[cols];
        int[] dirtDepth = new int[cols];

        double phase1 = (seed % 1000) * 0.01;
        double phase2 = ((seed / 7) % 1000) * 0.01;

        for (int col = 0; col < cols; col++) {
            double n1 = Math.sin(col * 0.10 + phase1) * 4.0;
            double n2 = Math.sin(col * 0.035 + phase2) * 2.0;
            int s = 14 + (int) Math.round(n1 + n2);

            s = Math.max(8, Math.min(rows - 15, s));
            surface[col] = s;

            dirtDepth[col] = 2 + random.nextInt(3); // 2..4
        }

        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                int x = col * BLOCK_SIZE;
                int y = row * BLOCK_SIZE;

                int s = surface[col];
                int dDepth = dirtDepth[col];

                if (row < s) continue;

                if (row == s) {
                    blocks.add(new Block(x, y, BLOCK_SIZE, World.Map.Block.GRASS));
                } else if (row <= s + dDepth) {
                    blocks.add(new Block(x, y, BLOCK_SIZE, World.Map.Block.DIRT));
                } else {
                    World.Map.Block type = chooseUndergroundPseudoOre(row, rows);
                    blocks.add(new Block(x, y, BLOCK_SIZE, type));
                }
            }
        }

        generateClouds(cols, surface);
    }

    private World.Map.Block chooseUndergroundPseudoOre(int row, int totalRows) {
        double depthFactor = (double) row / Math.max(1, totalRows);
        double roll = random.nextDouble();

        double coalChance = 0.06 + depthFactor * 0.04;
        double ironChance = 0.03 + depthFactor * 0.03;
        double goldChance = 0.012 + depthFactor * 0.018;
        double diamondChance = 0.004 + depthFactor * 0.01;

        if (roll < diamondChance) return World.Map.Block.GRASS;
        if (roll < diamondChance + goldChance) return World.Map.Block.LEAFS;
        if (roll < diamondChance + goldChance + ironChance) return World.Map.Block.DIRT;
        if (roll < diamondChance + goldChance + ironChance + coalChance) return World.Map.Block.WOOD;

        return World.Map.Block.STONE;
    }

    private void generateClouds(int cols, int[] surface) {
        int cloudCount = Math.max(8, cols / 18);

        int minSurface = Integer.MAX_VALUE;
        for (int s : surface) minSurface = Math.min(minSurface, s);

        for (int i = 0; i < cloudCount; i++) {
            int cloudW = 3 + random.nextInt(7);
            int cloudH = 1 + random.nextInt(2);

            int startCol = random.nextInt(Math.max(1, cols - cloudW));
            int maxSkyRow = Math.max(2, minSurface - 4);
            int cloudRow = 1 + random.nextInt(Math.max(1, maxSkyRow - 1));

            for (int dx = 0; dx < cloudW; dx++) {
                for (int dy = 0; dy < cloudH; dy++) {
                    double nx = (dx - cloudW / 2.0) / (cloudW / 2.0 + 0.001);
                    double ny = (dy - cloudH / 2.0) / (cloudH / 2.0 + 0.001);
                    if (nx * nx + ny * ny > 1.2) continue;

                    int col = startCol + dx;
                    int row = cloudRow + dy;
                    if (col < 0 || row < 0) continue;

                    if (row < surface[Math.min(col, surface.length - 1)]) {
                        int x = col * BLOCK_SIZE;
                        int y = row * BLOCK_SIZE;
                        if (!hasAnyBlockAt(x, y)) {
                            blocks.add(new Block(x, y, BLOCK_SIZE, World.Map.Block.CLOUD));
                        }
                    }
                }
            }
        }
    }

    private boolean hasAnyBlockAt(int x, int y) {
        Rectangle r = new Rectangle(x, y, BLOCK_SIZE, BLOCK_SIZE);
        for (Block b : blocks) {
            if (b.getBounds().intersects(r)) return true;
        }
        return false;
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public Block getBlockAt(Point point) {
        for (Block block : blocks) {
            if (block.getBounds().contains(point)) return block;
        }
        return null;
    }

    public boolean removeBlock(Block block) {
        return blocks.remove(block);
    }

    public boolean placeBlock(int pixelX, int pixelY, World.Map.Block type, Rectangle forbiddenArea) {
        int snappedX = (pixelX / BLOCK_SIZE) * BLOCK_SIZE;
        int snappedY = (pixelY / BLOCK_SIZE) * BLOCK_SIZE;

        if (snappedX < 0 || snappedY < 0) return false;
        if (snappedX >= worldPixelWidth || snappedY >= worldPixelHeight) return false;

        Rectangle newBlockBounds = new Rectangle(snappedX, snappedY, BLOCK_SIZE, BLOCK_SIZE);
        if (forbiddenArea != null && newBlockBounds.intersects(forbiddenArea)) return false;

        for (Block block : blocks) {
            if (block.getBounds().intersects(newBlockBounds)) return false;
        }

        blocks.add(new Block(snappedX, snappedY, BLOCK_SIZE, type));
        return true;
    }

    public void draw(Graphics g) {
        for (Block block : blocks) {
            block.draw(g);
        }
    }
}