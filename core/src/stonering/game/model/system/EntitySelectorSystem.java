package stonering.game.model.system;

import com.badlogic.gdx.graphics.Color;
import stonering.entity.RenderAspect;
import stonering.entity.unit.aspects.OrientationAspect;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.buildings.BuildingType;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.local_map.LocalMap;
import stonering.stage.renderer.AtlasesEnum;
import stonering.stage.toolbar.Toolbar;
import stonering.util.geometry.Position;
import stonering.util.geometry.RotationUtil;
import stonering.util.global.Logger;

import static stonering.enums.OrientationEnum.N;

/**
 * System that handles input passed to {@link EntitySelector}, does movement, validation and activation of selector.
 * On movement validation and render aspects are updated.
 * All aspects are updated on confirming and cancelling selection
 * Validation and activation is made in selector's aspects.
 *
 * @author Alexander on 10.01.2020
 */
public class EntitySelectorSystem implements ModelComponent {
    private final Color selectionInvalidColor = new Color(1, 0, 0, 0.5f);
    public final EntitySelector selector;
    public final EntitySelectorInputHandler inputHandler;

    public EntitySelectorSystem() {
        selector = new EntitySelector(new Position());
        selector.addAspect(new SelectionAspect(selector));
        selector.addAspect(new RenderAspect(selector, 0, 2, AtlasesEnum.ui_tiles));
        selector.addAspect(new OrientationAspect(selector, N));
        inputHandler = new EntitySelectorInputHandler(this);
    }

    public void validateAndUpdate() {
        SelectionAspect aspect = selector.getAspect(SelectionAspect.class);
        aspect.validator.validateAnd(selector.position, bool -> {
            if (!bool) selector.getAspect(RenderAspect.class).color = selectionInvalidColor;
        }); //TODO update render
    }

    public void handleSelection() {
        SelectionAspect aspect = selector.getAspect(SelectionAspect.class);
        if (aspect.selectHandler != null) aspect.selectHandler.accept(aspect.getBox());
    }

    public void handleCancel() {
        selector.getAspect(SelectionAspect.class).boxStart = null;
        SelectionAspect aspect = selector.getAspect(SelectionAspect.class);
        if (aspect.cancelHandler != null) aspect.cancelHandler.run();
        Toolbar toolbar = GameMvc.view().toolbarStage.toolbar;
        toolbar.removeSubMenus(toolbar.parentMenu);
    }

    public void resetSelector() {
        Logger.UI.logDebug("EntitySelector reset.");
        selector.getAspect(RenderAspect.class).region = AtlasesEnum.ui_tiles.getBlockTile(0, 2);
        SelectionAspect aspect = selector.getAspect(SelectionAspect.class);
        aspect.selectHandler = position -> GameMvc.view().showEntityStage(aspect.getBox());
        aspect.validator = position -> true;
        inputHandler.allowChangingZLevelOnSelection = true;
    }

    public void selectorMoved() {
        validateAndUpdate();
        GameMvc.view().localWorldStage.getCamera().handleSelectorMove();
    }

    public void placeSelectorAtMapCenter() {
        LocalMap localMap = GameMvc.model().get(LocalMap.class);
        selector.position.x = localMap.xSize / 2;
        selector.position.y = localMap.ySize / 2;
        for (int z = localMap.zSize - 1; z >= 0; z--) {
            if (localMap.getBlockType(selector.position.x, selector.position.y, z) != BlockTypeEnum.SPACE.CODE) {
                selector.position.z = z;
                break;
            }
        }
        selectorMoved();
    }

    public void rotateSelector(boolean clockwise) {
        Logger.UI.logDebug("rotating selector " + (clockwise ? "" : "counter") + " clockwise");
        BuildingType type = selector.getAspect(SelectionAspect.class).type;
        if (type == null || type.construction) return; // no rotation for constructions
        OrientationAspect orientationAspect = selector.getAspect(OrientationAspect.class);
        orientationAspect.current = RotationUtil.rotate(orientationAspect.current, clockwise);
        selector.getAspect(RenderAspect.class).region = AtlasesEnum.buildings.getBlockTile(type.sprites[orientationAspect.current.ordinal()]);
    }
}