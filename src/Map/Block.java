package Map;

import java.awt.Color;

public enum Block {

    // Blockname(blockColor, isSolid)
    AIR(new Color(0, 0, 0, 0), false),
    GRASS(new Color(106, 176, 76), true),
    DIRT(new Color(139, 90, 43), true),
    STONE(new Color(128, 128, 128), true),
    WOOD(new Color(101, 67, 33), true),
    LEAFS(new Color(34, 139, 34), false);

    public final Color blockColor;
    public final boolean isSolid;

    Block(Color blockColor, boolean isSolid) {
        this.blockColor = blockColor;
        this.isSolid = isSolid;
    }
}
