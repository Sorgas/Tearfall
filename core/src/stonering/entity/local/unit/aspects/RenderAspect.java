package stonering.entity.local.unit.aspects;

import com.badlogic.gdx.graphics.Texture;
import stonering.entity.local.Aspect;
import stonering.entity.local.Entity;

/**
 * Stores Unit's render information.
 * //TODO add texture.
 */
public class RenderAspect extends Aspect {
    public static String NAME = "render";

    private int[] atlasXY;
    private Texture texture;

    public RenderAspect(Entity entity) {
        super(entity);
    }

    public int[] getAtlasXY() {
        return atlasXY;
    }

    public void setAtlasXY(int[] atlasXY) {
        this.atlasXY = atlasXY;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
