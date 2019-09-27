package stonering.stage.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.HashMap;
import java.util.Map;

import static stonering.stage.renderer.BatchUtil.*;

/**
 * Contains all textures used for render. See {@link TileRenderer}, {@link DrawingUtil}
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
    items(new Texture("sprites/items.png"), false, 64, 64, 0, 0),
    substrates(new Texture("sprites/substrates.png"), true, 64, 64, 32, 6), // flat plants like mosses TODO remove topings
    liquids(new Texture("sprites/liquids.png"), true, 64, 64, 32, 6);

    public final Texture atlas;
    public final boolean hasToppings;
    public final Map<Position, TextureRegion> spriteCache;
    public final int WIDTH;
    public final int DEPTH;
    public final int BLOCK_HEIGHT; // depth and
    public final int TOPPING_BLOCK_HEIGTH;
    public final int TOPPING_TILE_HEIGHT
    private Position cachePosition;

    AtlasesEnum(Texture texture, boolean hasToppings, int width, int depth, int height, int toppingHeight) {
        atlas = texture;
        this.hasToppings = hasToppings;
        WIDTH = width;
        DEPTH = depth;
        HEIGHT = height;
        TOPPING_HEIGTH = toppingHeight;
        spriteCache = new HashMap<>();
        cachePosition = new Position();
    }

    /**
     * Cuts main part of a block tile from x y position in specified atlas.
     * Handles atlases with no toppings.
     */
    public TextureRegion getBlockTile(int x, int y) {
        if (!spriteCache.containsKey(cachePosition.set(x, y, 0))) {
            int atlasY = hasToppings ? y * (BLOCK_TILE_HEIGHT) + TOPING_TILE_HEIGHT : y * (HEIGHT + DEPTH); // consider toppings or not
            int astlasY = hasToppings ? y * (BLOCK_TILE_HEIGHT) + TOPING_TILE_HEIGHT : y * (HEIGHT + DEPTH); // consider toppings or not
            spriteCache.put(cachePosition, new TextureRegion(atlas,
                    x * WIDTH,
                    atlasY,
                    WIDTH, TILE_HEIGHT));
        }
        return spriteCache.get(cachePosition);
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
        if (!spriteCache.containsKey(cachePosition.set(x, y, 1))) {
            spriteCache.put(cachePosition, new TextureRegion(atlas,
                    x * WIDTH,
                    y * BLOCK_TILE_HEIGHT,
                    WIDTH, TOPING_TILE_HEIGHT));
        }
        return spriteCache.get(cachePosition);
    }

    private int getAtlasY(int y) {
        return hasToppings ?
    }

    public TextureRegion getTopingTile(int[] xy) {
        return getTopingTile(xy[0], xy[1]);
    }
}

