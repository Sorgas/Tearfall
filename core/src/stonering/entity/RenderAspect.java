package stonering.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import stonering.entity.unit.aspects.CreatureStatusIcon;
import stonering.stage.renderer.AtlasesEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores Entity's render information.
 * <p>
 * //TODO add texture.
 */
public class RenderAspect extends Aspect {
    public final List<CreatureStatusIcon> icons = new ArrayList<>();
    public TextureRegion region;
    public float rotation = 0;
    public Color color;

    public RenderAspect(TextureRegion region) {
        this.region = region;
    }

    public RenderAspect(Entity entity, TextureRegion region) {
        super(entity);
        this.region = region;
    }

    public RenderAspect(Entity entity, int x, int y, int width, int height, AtlasesEnum atlas) {
        this(entity, atlas.getBlockTile(x, y, width, height));
    }

    public RenderAspect(Entity entity, int x, int y, AtlasesEnum atlas) {
        this(entity, x, y, 1, 1, atlas);
    }

    public RenderAspect(Entity entity, int[] xy, AtlasesEnum atlas) {
        this(entity, xy[0], xy[1], atlas);
    }

    public RenderAspect(AtlasesEnum atlas, String textureName, int x, int y) {
        this(atlas.getBlockTile(textureName, x, y, 1, 1));
    }

    ;
}
