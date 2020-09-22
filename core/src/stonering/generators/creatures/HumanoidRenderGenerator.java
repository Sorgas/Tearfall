package stonering.generators.creatures;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import stonering.entity.unit.aspects.HumanoidRenderAspect;
import stonering.enums.unit.race.CombinedAppearanceRange;
import stonering.enums.unit.race.CreatureType;
import stonering.stage.renderer.atlas.AtlasesEnum;

/**
 * Generates render info specific to humanoids.
 *
 * @author Alexander on 17.08.2020.
 */
public class HumanoidRenderGenerator {
    Random random = new Random();

    public HumanoidRenderAspect generateRender(CreatureType type, boolean male) {
        int bodyVariant = selectVariant(male, type.combinedAppearance.bodyRange);
        TextureRegion body = AtlasesEnum.humanoid_body.getBlockTile(bodyVariant, type.combinedAppearance.bodyRange.y);
        TextureRegion head = selectPart(male, type.combinedAppearance.headRange, AtlasesEnum.humanoid_head);
        TextureRegion foot = selectPart(male, type.combinedAppearance.footRange, AtlasesEnum.humanoid_foot);
        HumanoidRenderAspect aspect = new HumanoidRenderAspect(body, head, foot);
        aspect.bodyWidth = type.combinedAppearance.bodyRange.width.get(bodyVariant);
        aspect.bodyHeight = type.combinedAppearance.bodyHeight;
        return aspect;
    }

    public TextureRegion selectPart(boolean male, CombinedAppearanceRange range, AtlasesEnum atlas) {
        int x = selectVariant(male, range);
        return atlas.getBlockTile(x, range.y);
    }

    private int selectVariant(boolean male, CombinedAppearanceRange range) {
        return male ? random.nextInt(range.maleVariants) : random.nextInt(range.femaleVariants) + range.maleVariants;
    }
}
