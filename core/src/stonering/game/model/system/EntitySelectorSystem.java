package stonering.game.model.system;

import stonering.entity.unit.aspects.RenderAspect;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.local_map.LocalMap;
import stonering.stage.renderer.AtlasesEnum;
import stonering.stage.toolbar.menus.Toolbar;
import stonering.util.geometry.Int3dBounds;
import stonering.util.geometry.Position;
import stonering.util.validation.PositionValidator;

import java.util.function.Consumer;

/**
 * System that handles input passed to {@link EntitySelector}, does movement, validation and activation of selector.
 * On movement validation and render aspects are updated.
 * All aspects are updated on confirming and cancelling selection
 * Validation and activation is made in selector's aspects.
 *
 * @author Alexander on 10.01.2020
 */
public class EntitySelectorSystem implements ModelComponent {
    public final EntitySelector selector;
    public final EntitySelectorInputHandler inputHandler;

    public EntitySelectorSystem() {
        selector = new EntitySelector(new Position());
        selector.addAspect(new SelectionAspect(selector));
        selector.addAspect(new RenderAspect(selector, 0, 2, AtlasesEnum.ui_tiles));
        inputHandler = new EntitySelectorInputHandler(this);
    }

    public void validateAndUpdate() {
        //TODO update render
        SelectionAspect aspect = selector.getAspect(SelectionAspect.class);
        if (aspect.validator != null) aspect.validator.validateAnd(selector.position, bool -> {});
    }

    /**
     * Calls selectHandler for all tiles in selection box that are valid with validator.
     */
    public void handleSelection() {
        SelectionAspect aspect = selector.getAspect(SelectionAspect.class);
        PositionValidator validator = aspect.validator; // validates each position in box
        Consumer<Int3dBounds> handler = aspect.selectHandler; // called for each position
        handler.accept(aspect.getBox());
    }

    public void handleCancel() {
        selector.getAspect(SelectionAspect.class).boxStart = null;
        SelectionAspect aspect = selector.getAspect(SelectionAspect.class);
        if(aspect.cancelHandler != null) aspect.cancelHandler.accept(selector.position);
        Toolbar toolbar = GameMvc.instance().view().toolbarStage.toolbar;
        toolbar.hideSubMenus(toolbar.parentMenu);
    }

    public void selectorMoved() {
        validateAndUpdate();
        GameMvc.instance().view().localWorldStage.getCamera().handleSelectorMove();
    }

    public void setPositionValidator(PositionValidator validator) {
        selector.getAspect(SelectionAspect.class).validator = validator;
    }

    public void placeSelectorAtMapCenter() {
        LocalMap localMap = GameMvc.instance().model().get(LocalMap.class);
        selector.position.x = localMap.xSize / 2;
        selector.position.y = localMap.ySize / 2;
        for (int z = localMap.zSize - 1; z >= 0; z--) {
            if (localMap.getBlockType(selector.position.x, selector.position.y, z) != BlockTypesEnum.SPACE.CODE) {
                selector.position.z = z;
                break;
            }
        }
        selectorMoved();
    }
}