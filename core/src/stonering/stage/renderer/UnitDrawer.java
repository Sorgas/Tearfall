package stonering.stage.renderer;

import com.badlogic.gdx.graphics.Color;
import stonering.GameSettings;
import stonering.entity.job.action.Action;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.CreatureStatusIcon;
import stonering.entity.unit.aspects.PlanningAspect;
import stonering.entity.RenderAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.unit.UnitContainer;

import java.util.List;

import static stonering.stage.renderer.AtlasesEnum.creature_icons;

/**
 * Renders all units in a certain position.
 * Calculates position to draw unit, basing on unit's movement progress.
 * Draws units state icons and action progress bar.
 *
 * @author Alexander on 03.01.2020.
 */
public class UnitDrawer extends Drawer {
    private UnitContainer unitContainer;
    private int progressBarWidth = 56;

    public UnitDrawer(SpriteDrawingUtil spriteUtil, ShapeDrawingUtil shapeUtil) {
        super(spriteUtil, shapeUtil);
        unitContainer = GameMvc.model().get(UnitContainer.class);
    }

    public void drawUnits(int x, int y, int z) {
        if (unitContainer == null) return;
        for (Unit unit : unitContainer.getUnitsInPosition(x, y, z)) {
            RenderAspect aspect = unit.get(RenderAspect.class);
            spriteUtil.drawSprite(aspect.region, unit.vectorPosition);
            List<CreatureStatusIcon> icons = aspect.icons;
            for (int i = 0; i < icons.size(); i++) {
                spriteUtil.drawIcon(creature_icons.getBlockTile(icons.get(i).x, icons.get(i).y), unit.vectorPosition, i);
            }
            drawActionProgressBar(x, y, z, unit);
        }
    }

    private void drawActionProgressBar(int x, int y, int z, Unit unit) {
        Action action;
        if ("1".equals(GameSettings.DRAW_ACTION_PROGRESS.get())
                && unit.has(PlanningAspect.class)
                && (action = unit.get(PlanningAspect.class).getNextAction()) != null
                && action.progress > 0) {
            shapeUtil.drawRectangle(unit.vectorPosition, 4, 16, (int) (progressBarWidth * (action.progress / action.maxProgress)), 8, Color.WHITE);
        }
    }
}