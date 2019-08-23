package stonering.game.view.render.stages.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.util.geometry.Position;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains all textures used for render. See {@link TileRenderer}, {@link DrawingUtil}
 * TODO create sprite descriptor for key (x, y, color).
 *
 * @author Alexander on 03.08.2019.
 */
public enum AtlasesEnum {
    blocks(new Texture("sprites/blocks.png"), true),
    plants(new Texture("sprites/plants.png"), true),
    units(new Texture("sprites/units.png"), true),
    buildings(new Texture("sprites/buildings.png"), true),
    ui_tiles(new Texture("sprites/ui_tiles.png"), true),
    items(new Texture("sprites/items.png"), true),
    substrates(new Texture("sprites/substrates.png"), true),
    liquids(new Texture("sprites/liquids.png"), true);

    public final Texture atlas;
    public final boolean hasToppings;
    public final Map<Position, TextureRegion> spriteCache;

    AtlasesEnum(Texture texture, boolean hasToppings) {
        atlas = texture;
        this.hasToppings = hasToppings;
        spriteCache = new HashMap<>();
    }
}

