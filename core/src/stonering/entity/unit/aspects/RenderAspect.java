package stonering.entity.unit.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.stage.renderer.AtlasesEnum;

/**
 * Stores Unit's render information.
 * Used for rendering moving creatures 'between' the tiles.
 * //TODO add texture.
 */
public class RenderAspect extends Aspect {
    public final int[] atlasXY;
    public final AtlasesEnum atlas;
    public boolean needsVisible = true; // needs icons are visible only for controlled units.

    public RenderAspect(Entity entity, int x, int y, AtlasesEnum atlas) {
        super(entity);
        atlasXY = new int[]{x,y};
        this.atlas = atlas;
    }
}
