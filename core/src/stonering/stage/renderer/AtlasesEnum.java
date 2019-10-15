package stonering.stage.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import stonering.util.global.Logger;
import stonering.util.global.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains all textures used for render. See {@link TileRenderer}, {@link DrawingUtil}.
 * Logical tile (in game model) has width, height and depth. Texture tile has width and height(height + depth).
 * Also, for blocks, toppings are needed. Toppings has width and depth same as main tile, and reduced height.
 * TODO create sprite descriptor for key (x, y, color).
 *
 * @author Alexander on 03.08.2019.
 */
public enum AtlasesEnum {
    blocks(new Texture("sprites/blocks.png"), true, 64, 64, 32, 6), // regular map blocks
    ui_tiles(new Texture("sprites/ui_tiles.png"), true, 64, 64, 32, 6), // frame, selector, zones TODO move designation to icons
    substrates(new Texture("sprites/substrates.png"), true, 64, 64, 32, 6), // flat plants like mosses TODO remove toppings
    liquids(new Texture("sprites/liquids.png"), true, 64, 64, 32, 6),
    plants(new Texture("sprites/plants.png"), false, 64, 64, 0, 0), // all trees is plants TODO remove toppings
    units(new Texture("sprites/units.png"), false, 64, 64, 0, 0), //TODO remove toppings
    buildings(new Texture("sprites/buildings.png"), false, 64, 64, 32, 0), // buildings and furniture
    items(new Texture("sprites/items.png"), false, 32, 32, 0, 0),
    creature_icons(new Texture("sprites/creature_icons.png"), false, 16, 16, 0, 0),
    icons(new Texture("sprites/creature_icons.png"), false, 32, 32, 0, 0); // ui and designation icons

    public final Texture atlas;
    public final boolean hasToppings;
    public final int WIDTH;
    public final int DEPTH;
    public final int HEIGHT;
    public final int BLOCK_HEIGHT; // depth and height
    public final int TOPPING_BLOCK_HEIGHT; // depth and height
    public final int FULL_TILE_HEIGHT;
    public final int X_CORRECTION; // batch grid are 64x64, but some atlas tiles are smaller, correction is offset from left bottom corner of grid
    public final int Y_CORRECTION;
    public final Map<SpriteDescriptor, TextureRegion> spriteCache;

    AtlasesEnum(Texture texture, boolean hasToppings, int width, int depth, int height, int toppingHeight) {
        atlas = texture;
        this.hasToppings = hasToppings;
        WIDTH = width;
        DEPTH = depth;
        HEIGHT = height;
        BLOCK_HEIGHT = height + depth;
        TOPPING_BLOCK_HEIGHT = hasToppings ? toppingHeight + depth : 0;
        FULL_TILE_HEIGHT = BLOCK_HEIGHT + TOPPING_BLOCK_HEIGHT;
        X_CORRECTION = (BatchUtil.TILE_WIDTH - WIDTH) / 2;
        Y_CORRECTION = (BatchUtil.TILE_DEPTH - DEPTH) / 2;
        spriteCache = new HashMap<>();
    }

    /**
     * Cuts main part of a block tile from x y position in specified atlas.
     */
    public TextureRegion getBlockTile(int x, int y) {
        SpriteDescriptor key = new SpriteDescriptor(x, y, Color.WHITE.toIntBits(), false);
        if (!spriteCache.containsKey(key)) {
            int atlasY = y * FULL_TILE_HEIGHT + (hasToppings ? TOPPING_BLOCK_HEIGHT : 0); // consider toppings or not
            spriteCache.put(key, new TextureRegion(atlas, x * WIDTH, atlasY, WIDTH, BLOCK_HEIGHT));
        }
        return spriteCache.get(key);
    }

    public TextureRegion getBlockTile(int[] xy) {
        return getBlockTile(xy[0], xy[1]);
    }

    /**
     * Cuts topping part of a block tile from x y position in specified atlas.
     * Atlas should have toppings.
     */
    public TextureRegion getToppingTile(int x, int y) {
        if (!hasToppings) {
            Logger.RENDER.logError("Attempt to get topping from atlas without toppings.");
            return null;
        }
        SpriteDescriptor key = new SpriteDescriptor(x, y, Color.WHITE.toIntBits(), true);
        spriteCache.putIfAbsent(key, new TextureRegion(atlas, x * WIDTH, y * FULL_TILE_HEIGHT, WIDTH, TOPPING_BLOCK_HEIGHT));
        return spriteCache.get(key);
    }
}
