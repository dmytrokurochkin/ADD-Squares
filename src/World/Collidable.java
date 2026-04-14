package World;

import java.awt.Rectangle;

//interface of colidable objects
public interface Collidable {
    Rectangle getBounds();

    boolean isSolid();
}
