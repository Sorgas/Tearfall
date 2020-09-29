package stonering.stage.renderer.atlas;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import stonering.stage.renderer.TileKey;

/**
 * Stores texture and it's grid size, produces and caches texture regions.
 *
 * @author Alexander on 22.09.2020.
 */
public class TextureCache {
    protected final Texture atlas;
    protected final Map<TileKey, TextureRegion> tileCache = new HashMap<>();
    protected final TileKey cacheDescriptor = new TileKey(1, 1, 1, 1, 1);

    public final int TILE_WIDTH;
    public final int TILE_HEIGHT;

    public TextureCache(Texture atlas, AtlasesEnum atlasType) {
        this.atlas = atlas;
        TILE_WIDTH = atlasType.WIDTH;
        TILE_HEIGHT = atlasType.TILE_HEIGHT;
    }

    public TextureRegion getTile(int x, int y, int width, int height) {
        return tileCache.computeIfAbsent(new TileKey(x, y, width, height, Color.WHITE.toIntBits()), key1 -> new TextureRegion(atlas,
                x * TILE_WIDTH,
                y * TILE_HEIGHT, // skip topping of current tile
                TILE_WIDTH * width,
                TILE_HEIGHT * height));
    }

    protected TextureRegion retrieveFromCache(TileKey key, Map<TileKey, TextureRegion> cache, Function<TileKey, TextureRegion> provider) {
        return cache.computeIfAbsent(key, key1 -> provider.apply(key));
    }

    public TextureRegion getToppingTile(int x, int y) {
        return null;
    }
}
