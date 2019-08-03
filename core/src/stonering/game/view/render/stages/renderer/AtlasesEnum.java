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
    blocks(new Texture("sprites/blocks.png")),
    plants(new Texture("sprites/plants.png")),
    units(new Texture("sprites/units.png")),
    buildings(new Texture("sprites/buildings.png")),
    ui_tiles(new Texture("sprites/ui_tiles.png")),
    items(new Texture("sprites/items.png")),
    substrates(new Texture("sprites/substrates.png")),
    liquids(new Texture("sprites/liquids.png"));

    public final Texture atlas;
    public final Map<Position, TextureRegion> spriteCache;

    AtlasesEnum(Texture texture) {
        atlas = texture;
        spriteCache = new HashMap<>();
    }
}

