package stonering.generators.creatures;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import stonering.entity.RenderAspect;
import stonering.enums.unit.race.CombinedAppearanceRange;
import stonering.enums.unit.race.CreatureType;
import stonering.stage.renderer.atlas.AtlasesEnum;
import stonering.util.lang.Pair;
import stonering.util.sprite.TextureRegionCombiner;

/**
 * Generates render info specific to humanoids.
 *
 * @author Alexander on 17.08.2020.
 */
public class HumanoidRenderGenerator {
    private TextureRegionCombiner combiner = new TextureRegionCombiner();
    private Random random = new Random();

    public RenderAspect generateRender(CreatureType type, boolean male) {
        int bodyVariant = selectVariant(male, type.combinedAppearance.bodyRange);
        TextureRegion body = AtlasesEnum.humanoid_body.getBlockTile(bodyVariant, type.combinedAppearance.bodyRange.y);
        TextureRegion head = selectPart(male, type.combinedAppearance.headRange, AtlasesEnum.humanoid_head);
        TextureRegion foot = selectPart(male, type.combinedAppearance.footRange, AtlasesEnum.humanoid_foot);

        int tileWidth = AtlasesEnum.units.WIDTH;
        int bodyWidth = type.combinedAppearance.bodyRange.width.get(bodyVariant);
        int bodyHeight = type.combinedAppearance.bodyHeight;
        int footWidth = AtlasesEnum.humanoid_foot.WIDTH;
        List<Pair<TextureRegion, Vector2>> regions = new ArrayList<>();
        regions.add(new Pair<>(foot, new Vector2((tileWidth - bodyWidth) / 2f, 16)));
        regions.add(new Pair<>(foot, new Vector2((tileWidth + bodyWidth) / 2f - footWidth, 16)));
        regions.add(new Pair<>(body, new Vector2(0, 26)));
        regions.add(new Pair<>(head, new Vector2(16, bodyHeight + 16)));
        return new RenderAspect(combiner.combine(regions));
    }

    public TextureRegion selectPart(boolean male, CombinedAppearanceRange range, AtlasesEnum atlas) {
        int x = selectVariant(male, range);
        return atlas.getBlockTile(x, range.y);
    }

    private int selectVariant(boolean male, CombinedAppearanceRange range) {
        return male ? random.nextInt(range.maleVariants) : random.nextInt(range.femaleVariants) + range.maleVariants;
    }
}
