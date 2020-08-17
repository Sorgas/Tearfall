package stonering.stage.renderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Alexander on 17.08.2020.
 */
public enum BodyPartTexturesEnum {
    head("head", 32, 32),
    body("body", 64, 72),
    arm("arm", 16, 16),
    foot("foot", 16, 16);
    
    public final TextureCache cache;
    
    BodyPartTexturesEnum(String texturePath, int width, int height) {
        Texture texture = new Texture("sprites/unit/humanoid/" + texturePath);
        cache = new TextureCache(texture, width, height);
    }
    
    public TextureRegion get(int x, int y) {
        return cache.getTile(x, y, cache.TILE_WIDTH, cache.TILE_HEIGHT);
    }
}
