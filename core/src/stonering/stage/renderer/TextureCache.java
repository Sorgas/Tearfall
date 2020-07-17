package stonering.stage.renderer;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Textures contains grid of rectangular tiles. Tile consists of main part and topping part above it.
 * 
 * @author Alexander on 05.06.2020.
 */
public class TextureCache {
    private final Texture atlas;
    public final Map<TileSpriteDescriptor, TextureRegion> spriteCache;
    
    public final int TILE_WIDTH;
    public final int TILE_HEIGHT;
    public final int TOPPING_TILE_HEIGHT;
    public final int FULL_TILE_HEIGHT;
    
    public TextureCache(Texture atlas, AtlasesEnum atlasType) {
        this.atlas = atlas;
        spriteCache = new HashMap<>();
        this.TILE_WIDTH = atlasType.WIDTH;
        this.TILE_HEIGHT = atlasType.BLOCK_HEIGHT;
        this.TOPPING_TILE_HEIGHT = atlasType.TOPPING_TILE_HEIGHT;
        this.FULL_TILE_HEIGHT = atlasType.FULL_TILE_HEIGHT;
    }
    
    public TextureRegion getTile(int x, int y, int width, int height) {
        TileSpriteDescriptor key = new TileSpriteDescriptor(x, y, width, height, Color.WHITE.toIntBits(), false);
        return spriteCache.computeIfAbsent(key, key1 -> new TextureRegion(atlas,
                x * TILE_WIDTH,
                y * FULL_TILE_HEIGHT + TOPPING_TILE_HEIGHT, // consider toppings or not
                TILE_WIDTH * width,
                TILE_HEIGHT * height));
    }

    public TextureRegion getToppingTile(int x, int y) {
        TileSpriteDescriptor key = new TileSpriteDescriptor(x, y, 1, 1, Color.WHITE.toIntBits(), true);
        return spriteCache.computeIfAbsent(key , key1 -> new TextureRegion(atlas,
                x * TILE_WIDTH, 
                y * FULL_TILE_HEIGHT,
                TILE_WIDTH,
                TOPPING_TILE_HEIGHT));
    }

}
