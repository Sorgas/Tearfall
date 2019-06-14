package stonering.game.view.render.util;

import com.badlogic.gdx.math.Vector2;


/**
 * Represents visible area of tiles.
 *
 * @author Alexander_Kuzyakov on 14.06.2019.
 */
public class VisibleArea {
    public Vector2 cameraPosition;
    public Vector2 screenSize;
    private int z; // model z

    public VisibleArea(Vector2 cameraPosition, Vector2 screenSize) {
        this.cameraPosition = cameraPosition;
        this.screenSize = screenSize;
    }

    public int getMinY() {
        return cameraPosition.y - screenSize.y / 2;
    }

    public int getMaxY() {
        return cameraPosition.y + screenSize.y / 2;
    }


    public int getMinX() {
        return cameraPosition.x - screenSize.x / 2;
    }

    public int getMaxX() {
        return cameraPosition.x + screenSize.x / 2;
    }

    public int getMaxZ() {
        return z;
    }
}
