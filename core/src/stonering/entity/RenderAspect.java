package stonering.entity;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import stonering.entity.unit.aspects.CreatureStatusIcon;

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
}
