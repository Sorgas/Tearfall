package stonering.generators.creatures;

import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import stonering.entity.unit.aspects.HumanoidRenderAspect;
import stonering.enums.unit.CreatureType;
import stonering.stage.renderer.BodyPartTexturesEnum;

/**
 * Generates render info specific to humanoids.
 * 
 * @author Alexander on 17.08.2020.
 */
public class HumanoidRenderGenerator {
    Random random;
    
    public HumanoidRenderAspect generateRender(CreatureType type, boolean male) {
        TextureRegion body = selectBodyPart(type, male, type.combinedAppearance.body, BodyPartTexturesEnum.body);
        TextureRegion head = selectBodyPart(type, male, type.combinedAppearance.head, BodyPartTexturesEnum.head);
        TextureRegion foot = selectBodyPart(type, male, type.combinedAppearance.foot, BodyPartTexturesEnum.foot);
        HumanoidRenderAspect aspect = new HumanoidRenderAspect(body);
        aspect.head = head;
        aspect.foot = foot;
        return aspect;
    }
    
    public TextureRegion selectBodyPart(CreatureType type, boolean male, List<Integer> bounds, BodyPartTexturesEnum qwer) {
        int x = random.nextInt(bounds.get(male ? 1 : 2));
        if(!male) x += bounds.get(1);
        return qwer.get(x, bounds.get(0));
    }
}
