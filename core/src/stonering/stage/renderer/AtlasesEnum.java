package stonering.stage.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import org.jetbrains.annotations.NotNull;

import stonering.util.geometry.IntVector2;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains all textures used for render. See {@link TileDrawer}, {@link SpriteDrawingUtil}.
 * Logical tile (in game model) has width, height and depth. Texture tile has width and height(height + depth).
 * Also, for blocks, toppings are needed. Toppings has width and depth same as main tile, and reduced height.
 *
 * @author Alexander on 03.08.2019.
 */
public enum AtlasesEnum {
    blocks(new Texture("sprites/blocks.png"), true, 64, 64, 32, 6), // regular map blocks
    ui_tiles(new Texture("sprites/ui_tiles.png"), false, 64, 64, 32, 0), // frame, selector, zones TODO move designation to icons
    substrates(new Texture("sprites/substrates.png"), true, 64, 64, 32, 6), // flat plants like mosses TODO remove toppings
    liquids(new Texture("sprites/liquids.png"), true, 64, 64, 32, 6),
    plants(new Texture("sprites/plants.png"), false, 64, 64, 32, 0), // all trees is plants
    units(new Texture("sprites/units.png"), false, 64, 64, 0, 0),
    buildings("sprites/buildings", false, 64, 64, 32, 0), // buildings and furniture
    items(new Texture("sprites/items.png"), false, 32, 32, 0, 0),
    creature_icons(new Texture("sprites/creature_icons.png"), false, 16, 16, 0, 0),
    icons(new Texture("sprites/icons.png"), false, 64, 64, 0, 0); // ui and designation icons

    public TextureCache cache;
    public Map<String, TextureCache> caches = new HashMap<>();

    public String texturePath; // path to folder with texture files
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

    AtlasesEnum(boolean hasToppings, int width, int depth, int height, int toppingHeight) {
        this.hasToppings = hasToppings;
        WIDTH = width;
        DEPTH = depth;
        HEIGHT = height;
        BLOCK_HEIGHT = height + depth;
        TOPPING_HEIGHT = toppingHeight;
        TOPPING_BLOCK_HEIGHT = hasToppings ? TOPPING_HEIGHT + DEPTH : 0;
        FULL_TILE_HEIGHT = BLOCK_HEIGHT + TOPPING_BLOCK_HEIGHT;
        X_CORRECTION = (BatchUtil.TILE_WIDTH - WIDTH) / 2;
        Y_CORRECTION = (BatchUtil.TILE_DEPTH - DEPTH) / 2;
    }

    AtlasesEnum(Texture atlas, boolean hasToppings, int width, int depth, int height, int toppingHeight) {
        this(hasToppings, width, depth, height, toppingHeight);
        cache = new TextureCache(atlas, this);
    }

    AtlasesEnum(String texturePath, boolean hasToppings, int width, int depth, int height, int toppingHeight) {
        this(hasToppings, width, depth, height, toppingHeight);
        this.texturePath = texturePath;
        caches = new HashMap<>();
    }

    public TextureRegion getRegion(int x, int y, int width, int height) {
        return cache.getRegion(x, y, width, height);
    }

    public TextureRegion getRegion(String atlasName, int x, int y, int width, int height) {
        return caches.computeIfAbsent(atlasName, name -> new TextureCache(new Texture(texturePath + "/" + atlasName + ".png"), this))
                .getRegion(x, y, width, height);
    }

    /**
     * Cuts topping part of a block tile from x y position in specified atlas.
     * Atlas should have toppings.
     */
    public TextureRegion getToppingTile(int x, int y) {
        return cache.getToppingTile(x, y);
    }

    public TextureRegion getToppingTile(String atlasName, int x, int y) {
        return caches.computeIfAbsent(atlasName, name -> new TextureCache(new Texture(texturePath + "/" + atlasName + ".png"), this))
                .getToppingTile(x, y);
    }

    /**
     * Cuts main part of a block tile from x y position in specified atlas.
     */
    public TextureRegion getBlockTile(int x, int y) {
        return getRegion(x, y, 1, 1);
    }

    public TextureRegion getBlockTile(@NotNull int[] xy) {
        return getBlockTile(xy[0], xy[1]);
    }

    public TextureRegion getBlockTile(@NotNull IntVector2 vector) {
        return getBlockTile(vector.x, vector.y);
    }

    public TextureRegion getRegion(@NotNull IntVector2 xy, @NotNull IntVector2 size) {
        return getRegion(xy.x, xy.y, size.x, size.y);
    }

    public TextureRegion getRegion(@NotNull String atlasName, @NotNull IntVector2 xy, @NotNull IntVector2 size) {
        return getRegion(atlasName, xy.x, xy.y, size.x, size.y);
    }
}
