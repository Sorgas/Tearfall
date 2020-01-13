package stonering.game.model.system;

import stonering.entity.unit.aspects.RenderAspect;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.entity_selector.aspect.SelectorBoxAspect;
import stonering.game.model.entity_selector.aspect.ValidationAspect;
import stonering.game.model.local_map.LocalMap;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.geometry.Position;
import stonering.util.validation.PositionValidator;

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
    private Position cachePosition;

    public EntitySelectorSystem() {
        selector = new EntitySelector(new Position());
        selector.addAspect(new ValidationAspect(selector));
        selector.addAspect(new SelectorBoxAspect(selector));
        selector.addAspect(new SelectionAspect(selector));
        selector.addAspect(new RenderAspect(selector, 0, 2, AtlasesEnum.ui_tiles));
        inputHandler = new EntitySelectorInputHandler(this);
        cachePosition = new Position();
    }

    public void validateAndUpdate() {
        //TODO update render
        ValidationAspect aspect = selector.getAspect(ValidationAspect.class);
        if (aspect.validator != null) aspect.validator.validateAnd(selector.position, aspect.validationConsumer);
    }

    /**
     * Calls selectHandler for all tiles in selection box that are valid with {@link ValidationAspect}'s validator.
     */
    public void handleSelection() {
        SelectionAspect aspect = selector.getAspect(SelectionAspect.class);
        selector.getAspect(SelectorBoxAspect.class).boxIterator.accept(aspect.selectHandler != null ? aspect.selectHandler : aspect.defaultSelectHandler);
    }

    public void handleCancel() {
        selector.getAspect(SelectorBoxAspect.class).boxStart = null;
        selector.getAspect(SelectionAspect.class).cancelHandler.accept(selector.position);
        //TODO close toolbar menus
    }

    public void selectorMoved() {
        validateAndUpdate();
        GameMvc.instance().view().localWorldStage.getCamera().handleSelectorMove();
    }

    public void setPositionValidator(PositionValidator validator) {
        selector.getAspect(ValidationAspect.class).validator = validator;
    }

    public void placeSelectorAtMapCenter() {
        LocalMap localMap = GameMvc.instance().model().get(LocalMap.class);
        selector.position.x = localMap.xSize / 2;
        selector.position.y = localMap.ySize / 2;
        for (int z = localMap.zSize - 1; z >= 0; z--) {
            if (localMap.getBlockType(selector.position.x, selector.position.y, z) == BlockTypesEnum.SPACE.CODE)
                continue;
            selector.position.z = z;
        }
        selectorMoved();
    }
}