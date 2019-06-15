package stonering.game.view.render.stages.renderer;

/**
 * Class for casting model coordinates to batch and screen.
 *
 * @author Alexander on 15.06.2019.
 */
public class BatchUtil {
    public static final int TILE_WIDTH = 64;             // x size(left-right)
    public static final int TILE_DEPTH = 64;             // y size(back-forth)
    public static final int TILE_HEIGHT = 96;            // z size(up-down) plus depth
    public static final int TOPING_TILE_HEIGHT = 70;     // depth plus floor height(6)
    public static final int BLOCK_TILE_HEIGHT = 166;     // total block height

    public static float getBatchX(float x) {
        return x * TILE_WIDTH;
    }

    public static float getBatchY(float y, float z) {
        return y * TILE_DEPTH + z * (TILE_HEIGHT - TILE_DEPTH);
    }

    /**
     * @param z      model z
     * @param batchY batch coordinate
     * @return model y
     */
    public static int getModelY(int z, float batchY) {
        return (int) Math.ceil((batchY - z * (TILE_HEIGHT - TILE_DEPTH)) / TILE_DEPTH);
    }

    /**
     * @param batchX batch coordinate
     * @return model x
     */
    public static int getModelX(float batchX) {
        return (int) Math.ceil(batchX / TILE_WIDTH);
    }
}
