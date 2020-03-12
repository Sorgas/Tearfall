package stonering.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.entity.unit.aspects.CreatureStatusIcon;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.geometry.IntVector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores Entity's render information.
 * <p>
 * //TODO add texture.
 */
public class RenderAspect extends Aspect {
    public final AtlasesEnum atlas;
    public final IntVector2 atlasXY;
    public final IntVector2 size;
    public final List<CreatureStatusIcon> icons = new ArrayList<>();
    public TextureRegion region;
    public Color color;

    public RenderAspect(Entity entity, int x, int y, int width, int height, AtlasesEnum atlas) {
        super(entity);
        this.atlas = atlas;
        atlasXY = new IntVector2(x, y);
        size = new IntVector2(width, height);
        region = atlas.getRegion(x, y, width, height);
    }

    public RenderAspect(Entity entity, int x, int y, AtlasesEnum atlas) {
        this(entity, x, y, 1, 1, atlas);
    }

    public RenderAspect(Entity entity, int[] xy, AtlasesEnum atlas) {
        this(entity, xy[0], xy[1], atlas);
    }
}
