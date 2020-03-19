package stonering.stage.renderer;

import com.badlogic.gdx.math.Vector2;
import stonering.util.geometry.Position;

/**
 * Class for casting model coordinates to batch and screen.
 *
 * @author Alexander on 15.06.2019.
 */
public class BatchUtil {
    public static final int TILE_WIDTH = 64;             // x size(left-right)
    public static final int TILE_DEPTH = 64;             // y size(back-forth)
    public static final int TILE_HEIGHT = 32;            // z size(up-down) plus depth
    public static final int ICON_SIZE = 16; // TODO make configurable in settings

    /**
     * Left border of tile.
     */
    public static float getBatchX(float x) {
        return x * TILE_WIDTH;
    }

    /**
     * Bottom border of tile.
     */
    public static float getBatchY(float y, float z) {
        return y * TILE_DEPTH + z * TILE_HEIGHT;
    }

    public static float getBatchXForIcon(float x, int index) {
        return getBatchX(x) + index * ICON_SIZE;
    }

    public static float getBatchYForIcon(float y, float z) {
        return getBatchY(y, z) + TILE_DEPTH - ICON_SIZE;
    }

    /**
     * @param z      model z
     * @param batchY batch coordinate
     * @return model y
     */
    public static int getModelY(int z, float batchY) {
        return (int) Math.ceil((batchY - z * (TILE_HEIGHT)) / TILE_DEPTH);
    }

    /**
     * @param batchX batch coordinate
     * @return model x
     */
    public static int getModelX(float batchX) {
        return (int) Math.ceil(batchX / TILE_WIDTH);
    }

    /**
     * @param position Model position
     * @return Coordinates of bottom left corner of sprite.
     */
    public static Vector2 getBottomLeftCorner(Position position) {
        return new Vector2(getBatchX(position.x), getBatchY(position.y, position.z));
    }

    /**
     * @return Coordinates of right top corner of sprite.
     */
    public static Vector2 getRightTopCorner(int x, int y, int z) {
        return new Vector2(getBatchX(x + 1) - 1, getBatchY(y + 1, z) - 1);
    }
}
