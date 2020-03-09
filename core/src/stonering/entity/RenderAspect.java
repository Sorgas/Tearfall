package stonering.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.entity.unit.aspects.CreatureStatusIcon;
import stonering.stage.renderer.AtlasesEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores Entity's render information.
 *
 * //TODO add texture.
 */
public class RenderAspect extends Aspect {
    public final int[] atlasXY;
    public final AtlasesEnum atlas;
    public final List<CreatureStatusIcon> icons = new ArrayList<>();
    public TextureRegion region;
    public Color color;
    public float actionProgress;

    public RenderAspect(Entity entity, int[] xy, AtlasesEnum atlas) {
        super(entity);
        atlasXY = xy;
        this.atlas = atlas;
        region = atlas.getBlockTile(atlasXY);
        actionProgress = 0;
    }

    public RenderAspect(Entity entity, int x, int y, AtlasesEnum atlas) {
        this(entity, new int[]{x, y}, atlas);
    }
}