package stonering.entity.unit.aspects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import stonering.entity.RenderAspect;

/**
 * Render information for units.
 * Humanoid figure is combined of body, head, arm and feet sprites.
 * There are also sprites for clothes and tools.
 * 
 * @author Alexander on 17.08.2020.
 */
public class HumanoidRenderAspect extends RenderAspect {
    public TextureRegion head;
    public TextureRegion arm;
    public TextureRegion foot;
    public Sprite qwer;
    //TODO wear and tool sprites
    
    public HumanoidRenderAspect(TextureRegion region) {
        super(region);
    }
}
