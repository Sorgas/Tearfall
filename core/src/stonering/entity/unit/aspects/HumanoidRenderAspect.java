package stonering.entity.unit.aspects;

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
    public TextureRegion foot;
    public int bodyHeight;
    public int bodyWidth;
    //TODO wear and tool sprites
    //TODO add oriented sprites


    public HumanoidRenderAspect(TextureRegion body, TextureRegion head, TextureRegion foot) {
        super(body);
        this.head = head;
        this.foot = foot;
    }
}
