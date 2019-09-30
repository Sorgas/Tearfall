package stonering.stage.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.util.geometry.Position;
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
    plants(new Texture("sprites/plants.png"), true, 64, 64, 32, 6), // all trees is plants TODO remove topings
    units(new Texture("sprites/units.png"), true, 64, 64, 32, 6), //TODO remove topings
    buildings(new Texture("sprites/buildings.png"), true, 64, 64, 32, 6), // buildings and furniture
    ui_tiles(new Texture("sprites/ui_tiles.png"), true, 64, 64, 32, 6), // designations
    items(new Texture("sprites/items.png"), false, 32, 32, 0, 0),
    substrates(new Texture("sprites/substrates.png"), true, 64, 64, 32, 6), // flat plants like mosses TODO remove topings
    liquids(new Texture("sprites/liquids.png"), true, 64, 64, 32, 6);

    public final Texture atlas;
    public final boolean hasToppings;
    public final int WIDTH;
    public final int DEPTH;
    public final int HEIGHT;
    public final int BLOCK_HEIGHT; // depth and height
    public final int TOPPING_BLOCK_HEIGHT; // depth and height
    public final int FULL_TILE_HEIGHT;
    public final Map<Pair<Integer, Integer>, TextureRegion> spriteCache;
    private Position cachePosition;

    AtlasesEnum(Texture texture, boolean hasToppings, int width, int depth, int height, int toppingHeight) {
        atlas = texture;
        this.hasToppings = hasToppings;
        WIDTH = width;
        DEPTH = depth;
        HEIGHT = height;
        BLOCK_HEIGHT = depth + height;
        TOPPING_BLOCK_HEIGHT = toppingHeight + depth;
        FULL_TILE_HEIGHT = BLOCK_HEIGHT + TOPPING_BLOCK_HEIGHT;
        spriteCache = new HashMap<>();
        cachePosition = new Position();
    }

    /**
     * Cuts main part of a block tile from x y position in specified atlas.
     * Handles atlases with no toppings.
     */
    public TextureRegion getBlockTile(int x, int y) {
        Pair<Integer, Integer> key = new Pair<>(x, y);
        if (!spriteCache.containsKey(key)) {
            int atlasY = y * (hasToppings ? FULL_TILE_HEIGHT + TOPPING_BLOCK_HEIGHT : BLOCK_HEIGHT); // consider toppings or not
            spriteCache.put(key, new TextureRegion(atlas, x * WIDTH, atlasY, WIDTH, BLOCK_HEIGHT));
        }
        return spriteCache.get(key);
    }

    public TextureRegion getBlockTile(int[] xy) {
        return getBlockTile(xy[0], xy[1]);
    }

    /**
     * Cuts toping part of a block tile from x y position in specified atlas.
     * Atlas should have toppings.
     */
    public TextureRegion getTopingTile(int x, int y) {
        if (!hasToppings) {
            Logger.RENDER.logError("Attempt to get topping from atlas without toppings.");
            return null;
        }
        Pair<Integer, Integer> key = new Pair<>(x, y);
        spriteCache.putIfAbsent(key, new TextureRegion(atlas, x * WIDTH, y * BLOCK_HEIGHT, WIDTH, BLOCK_HEIGHT));
        return spriteCache.get(key);
    }
}
