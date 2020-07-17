package stonering.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;

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
    public AtlasesEnum atlas;
    public String textureName;
    public float rotation = 0;
    public Color color;

    public RenderAspect(TextureRegion region) {
        this.region = region;
    }

    public RenderAspect(@NotNull AtlasesEnum atlas, @Nullable String textureName, int x, int y) {
        this(atlas.getBlockTile(textureName, x, y));
        this.atlas = atlas;
        this.textureName = textureName;
    }

    public RenderAspect(AtlasesEnum atlas, int x, int y) {
        this(atlas, null, x, y);
    }
}
