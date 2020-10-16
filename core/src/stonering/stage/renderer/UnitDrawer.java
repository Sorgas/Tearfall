package stonering.stage.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import stonering.GameSettings;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.EatAction;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.CreatureStatusIcon;
import stonering.entity.unit.aspects.job.TaskAspect;
import stonering.entity.RenderAspect;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.unit.UnitContainer;
import stonering.stage.renderer.atlas.AtlasesEnum;
import stonering.util.geometry.Position;

import java.util.List;
import java.util.Optional;

import static stonering.stage.renderer.atlas.AtlasesEnum.creature_icons;

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
    private Vector3 cacheVector;
    private final float FLOOR_CORRECTION = AtlasesEnum.blocks.TOPPING_HEIGHT / (float) AtlasesEnum.blocks.DEPTH;

    public UnitDrawer(SpriteDrawingUtil spriteUtil, ShapeDrawingUtil shapeUtil) {
        super(spriteUtil, shapeUtil);
        unitContainer = GameMvc.model().get(UnitContainer.class);
        cacheVector = new Vector3();
    }

    public void drawUnits(int x, int y, int z) {
        Optional.ofNullable(unitContainer)
                .ifPresent(container -> container.getUnitsInPosition(x, y, z)
                        .forEach(unit -> {
                            drawUnit(unit);
                            drawHauledItem(unit);
                            drawActionProgressBar(x, y, z, unit);
                            drawEatenFoodItem(x, y, z, unit);
                        }));
    }

    private void drawUnit(Unit unit) {
        unit.optional(RenderAspect.class).ifPresent(aspect -> {
            if (aspect.rotation != 0) {
                spriteUtil.drawSpriteWithRotation(aspect.region, unit.vectorPosition, aspect.rotation);
            } else {
                spriteUtil.drawSprite(aspect.region, unit.vectorPosition);
            }
            List<CreatureStatusIcon> icons = aspect.icons;
            for (int i = 0; i < icons.size(); i++) {
                spriteUtil.drawIcon(creature_icons.getBlockTile(icons.get(i).x, icons.get(i).y), unit.vectorPosition, i);
            }
        });
    }

    private void drawIcons(Unit unit) {
        
    }
    
    private void drawHauledItem(Unit unit) {
        unit.optional(EquipmentAspect.class)
                .map(aspect -> aspect.hauledItem)
                .ifPresent(item -> {
                    cacheVector.set(unit.vectorPosition).add(0.25f, 0.25f, 0);
                    spriteUtil.drawSprite(item.get(RenderAspect.class).region, cacheVector);
                });
    }

    private void drawActionProgressBar(int x, int y, int z, Unit unit) {
        Action action;
        if (GameSettings.DRAW_ACTION_PROGRESS.VALUE > 0
                && unit.has(TaskAspect.class)
                && (action = unit.get(TaskAspect.class).getNextAction()) != null
                && action.progress > 0) {
            shapeUtil.drawRectangle(unit.vectorPosition, 4, 16, (int) (progressBarWidth * (action.progress / action.maxProgress)), 8, Color.WHITE);
        }
    }

    private void drawEatenFoodItem(int x, int y, int z, Unit unit) {
        unit.optional(TaskAspect.class)
                .map(aspect -> aspect.task)
                .map(task -> task.nextAction)
                .filter(action -> action instanceof EatAction)
                .map(action -> (EatAction) action)
                .filter(action -> action.started)
                .ifPresent(action -> {
                    if (action.tableBlock != null) {
                        Position position = action.tableBlock.position;
                        cacheVector.set(position.x, position.y, position.z).add(0.25f, 0.25f, 0);
                        spriteUtil.drawSprite(action.item.get(RenderAspect.class).region, cacheVector);
                    }
                });
    }
}