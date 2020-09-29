package stonering.stage.renderer.atlas;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import stonering.stage.renderer.TileKey;

/**
 * Stores tile grid texture and helps getting tiles from it.
 * Tile consists of main part and optional topping part above it.
 *
 * @author Alexander on 05.06.2020.
 */
public class ToppingTextureCache extends TextureCache {
    private final Map<TileKey, TextureRegion> toppingsCache = new HashMap<>();

    public final int TOPPING_TILE_HEIGHT;
    public final int FULL_TILE_HEIGHT;

    public ToppingTextureCache(Texture atlas, AtlasesEnum atlasType) {
        super(atlas, atlasType);
        TOPPING_TILE_HEIGHT = atlasType.TOPPING_TILE_HEIGHT;
        FULL_TILE_HEIGHT = TILE_HEIGHT + TOPPING_TILE_HEIGHT;
    }

    public TextureRegion getTile(int x, int y, int width, int height) {
        return tileCache.computeIfAbsent(new TileKey(x, y, width, height, Color.WHITE.toIntBits()), key1 -> new TextureRegion(atlas,
                x * TILE_WIDTH,
                y * FULL_TILE_HEIGHT + TOPPING_TILE_HEIGHT, // skip topping of current tile
                TILE_WIDTH * width,
                TILE_HEIGHT * height));
    }

    public TextureRegion getToppingTile(int x, int y) {
        return toppingsCache.computeIfAbsent(new TileKey(x, y, Color.WHITE.toIntBits()), key1 -> new TextureRegion(atlas,
                x * TILE_WIDTH,
                y * FULL_TILE_HEIGHT,
                TILE_WIDTH,
                TOPPING_TILE_HEIGHT));
    }
}
