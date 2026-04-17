package World;

import java.awt.Color;

public class Map {

    public enum Block {
        GRASS(new Color(95, 159, 53), true),
        DIRT(new Color(121, 85, 58), true),
        STONE(new Color(120, 120, 120), true),
        WOOD(new Color(156, 102, 31), true),
        LEAFS(new Color(66, 135, 57), true),

        COAL_ORE(new Color(70, 70, 70), true),
        IRON_ORE(new Color(184, 140, 96), true),
        GOLD_ORE(new Color(232, 191, 58), true),
        DIAMOND_ORE(new Color(72, 214, 230), true),

        CLOUD(new Color(200, 200, 200), false),

        AIR(new Color(0, 0, 0, 0), false);

        private final Color blockColor;
        private final boolean solid;

        Block(Color blockColor, boolean solid) {
            this.blockColor = blockColor;
            this.solid = solid;
        }

        public Color getBlockColor() {
            return blockColor;
        }

        public boolean isSolid() {
            return solid;
        }
    }
}