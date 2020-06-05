package stonering.stage.renderer;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import stonering.util.global.Logger;

/**
 * @author Alexander on 05.06.2020.
 */
public class TextureCache {
    private final Texture atlas;
    public final Map<TileSpriteDescriptor, TextureRegion> spriteCache;
    public final AtlasesEnum atlasType;

    public final boolean hasToppings;
    public final int WIDTH;
    public final int DEPTH;
    public final int HEIGHT;
    public final int BLOCK_HEIGHT; // depth and height
    public final int TOPPING_HEIGHT;
    public final int TOPPING_BLOCK_HEIGHT; // depth and height
    public final int FULL_TILE_HEIGHT;
    public final int X_CORRECTION; // batch grid are 64x64, but some atlas tiles are smaller, correction is offset from left bottom corner of grid
    public final int Y_CORRECTION;
    
    public TextureCache(Texture atlas, AtlasesEnum atlasType) {
        this.atlas = atlas;
        spriteCache = new HashMap<>();
        this.atlasType = atlasType;
        this.hasToppings = atlasType.hasToppings;
        this.WIDTH = atlasType.WIDTH;
        this.DEPTH = atlasType.DEPTH;
        this.HEIGHT = atlasType.HEIGHT;
        this.BLOCK_HEIGHT = atlasType.BLOCK_HEIGHT;
        this.TOPPING_HEIGHT = atlasType.TOPPING_HEIGHT;
        this.TOPPING_BLOCK_HEIGHT = atlasType.TOPPING_BLOCK_HEIGHT;
        this.FULL_TILE_HEIGHT = atlasType.FULL_TILE_HEIGHT;
        this.X_CORRECTION = atlasType.X_CORRECTION;
        this.Y_CORRECTION = atlasType.Y_CORRECTION;
    }
    
    /**
     * Supports multi-tile regions.
     */
    public TextureRegion getRegion(int x, int y, int width, int height) {
        TileSpriteDescriptor key = new TileSpriteDescriptor(x, y, width, height, Color.WHITE.toIntBits(), false);
        spriteCache.putIfAbsent(key, new TextureRegion(atlas,
                x * WIDTH,
                y * FULL_TILE_HEIGHT + (hasToppings ? TOPPING_BLOCK_HEIGHT : 0), // consider toppings or not
                WIDTH * width,
                BLOCK_HEIGHT * height));
        return spriteCache.get(key);
    }

    /**
     * Supports multi-tile regions.
     */
    public TextureRegion getRegion(String textureName, int x, int y, int width, int height) {
        TileSpriteDescriptor key = new TileSpriteDescriptor(x, y, width, height, Color.WHITE.toIntBits(), false);
        spriteCache.putIfAbsent(key, new TextureRegion(atlas,
                x * WIDTH,
                y * FULL_TILE_HEIGHT + (hasToppings ? TOPPING_BLOCK_HEIGHT : 0), // consider toppings or not
                WIDTH * width,
                BLOCK_HEIGHT * height));
        return spriteCache.get(key);
    }

    /**
     * Cuts topping part of a block tile from x y position in specified atlas.
     * Atlas should have toppings.
     */
    public TextureRegion getToppingTile(int x, int y) {
        if (!hasToppings) return Logger.RENDER.logError("Attempt to get topping from atlas without toppings.", null);
        TileSpriteDescriptor key = new TileSpriteDescriptor(x, y, 1, 1, Color.WHITE.toIntBits(), true);
        spriteCache.putIfAbsent(key, new TextureRegion(atlas,
                x * WIDTH,
                y * FULL_TILE_HEIGHT,
                WIDTH,
                TOPPING_BLOCK_HEIGHT));
        return spriteCache.get(key);
    }

}
