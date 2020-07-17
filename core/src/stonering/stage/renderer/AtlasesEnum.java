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
 * Tile consists of main part and topping part above it.
 *
 * @author Alexander on 03.08.2019.
 */
public enum AtlasesEnum {
    blocks(new Texture("sprites/blocks.png"), 64, 64, 32, 6), // regular map blocks
    ui_tiles(new Texture("sprites/ui_tiles.png"), 64, 64, 32), // frame, selector, zones TODO move designation to icons
    substrates(new Texture("sprites/substrates.png"), 64, 64, 32, 6), // flat plants like mosses TODO remove toppings
    liquids(new Texture("sprites/liquids.png"), 64, 64, 32, 6),
    plants("sprites/plants", 64, 64, 32), // all trees is plants
    units(new Texture("sprites/units.png"), 64, 64, 0),
    buildings("sprites/buildings", 64, 64, 32), // buildings and furniture
    items(new Texture("sprites/items.png"), 32, 32, 0),
    creature_icons(new Texture("sprites/creature_icons.png"), 16, 16, 0),
    zones(new Texture("sprites/zones.png"), 64, 64, 32),
    icons(new Texture("sprites/icons.png"), 64, 64, 0); // ui and designation icons

    public TextureCache cache; // for single textures
    public Map<String, TextureCache> caches = new HashMap<>(); // for multiple textures

    public String texturePath; // path to folder with texture files
    public final int WIDTH;
    public final int DEPTH;
    public final int HEIGHT;
    public final int TOPPING_HEIGHT;
    public final int TILE_HEIGHT; // depth and height
    public final int TOPPING_TILE_HEIGHT; // depth and height
    public final int FULL_TILE_HEIGHT;

    AtlasesEnum(Texture atlas, int width, int depth, int height, int toppingHeight) {
        this(width, depth, height, toppingHeight);
        cache = new TextureCache(atlas, this);
    }

    AtlasesEnum(String texturePath, int width, int depth, int height, int toppingHeight) {
        this(width, depth, height, toppingHeight);
        this.texturePath = texturePath;
        caches = new HashMap<>();
    }

    AtlasesEnum(Texture atlas, int width, int depth, int height) {
        this(atlas, width, depth, height, 0);
    }

    AtlasesEnum(String texturePath, int width, int depth, int height) {
        this(texturePath, width, depth, height, 0);
    }

    AtlasesEnum(int width, int depth, int height, int toppingHeight) {
        WIDTH = width;
        DEPTH = depth;
        HEIGHT = height;
        TILE_HEIGHT = height + depth;
        TOPPING_HEIGHT = toppingHeight;
        TOPPING_TILE_HEIGHT = TOPPING_HEIGHT != 0 ? TOPPING_HEIGHT + DEPTH : 0;
        FULL_TILE_HEIGHT = TILE_HEIGHT + TOPPING_TILE_HEIGHT;
    }

    public TextureRegion getBlockTile(int x, int y, int width, int height) {
        return cache.getTile(x, y, width, height);
    }

    public TextureRegion getBlockTile(String atlasName, int x, int y, int width, int height) {
        return caches.computeIfAbsent(atlasName, name -> new TextureCache(new Texture(texturePath + "/" + atlasName + ".png"), this))
                .getTile(x, y, width, height);
    }

    /**
     * Cuts main part of a block tile from x y position in specified atlas.
     */
    public TextureRegion getBlockTile(int x, int y) {
        return getBlockTile(x, y, 1, 1);
    }

    public TextureRegion getBlockTile(@NotNull int[] xy) {
        return getBlockTile(xy[0], xy[1]);
    }

    public TextureRegion getBlockTile(@NotNull IntVector2 vector) {
        return getBlockTile(vector.x, vector.y);
    }

    public TextureRegion getBlockTile(@NotNull IntVector2 xy, @NotNull IntVector2 size) {
        return getBlockTile(xy.x, xy.y, size.x, size.y);
    }

    public TextureRegion getBlockTile(@NotNull String atlasName, @NotNull IntVector2 xy, @NotNull IntVector2 size) {
        return getBlockTile(atlasName, xy.x, xy.y, size.x, size.y);
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
}
