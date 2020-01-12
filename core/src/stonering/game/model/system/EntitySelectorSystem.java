package stonering.game.model.system;

import com.badlogic.gdx.math.Vector3;
import stonering.entity.unit.aspects.RenderAspect;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;
import stonering.game.model.Updatable;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.entity_selector.aspect.SelectorBoxAspect;
import stonering.game.model.entity_selector.aspect.ValidationAspect;
import stonering.game.model.local_map.LocalMap;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.geometry.Position;
import stonering.util.global.Executor;
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
    private Position cachePosition;

    public EntitySelectorSystem() {
        selector = new EntitySelector(new Position());
        selector.addAspect(new ValidationAspect(selector));
        selector.addAspect(new SelectorBoxAspect(selector));
        selector.addAspect(new SelectionAspect(selector));
        selector.addAspect(new RenderAspect(selector, 0, 2, AtlasesEnum.ui_tiles));
        cachePosition = new Position();
    }

    public void validateAndUpdate() {
        //TODO update render
        ValidationAspect aspect = selector.getAspect(ValidationAspect.class);
        if (aspect.validator != null) aspect.validator.validateAnd(selector.position, aspect.validationConsumer);
    }

    public void handleSelection() {
        SelectionAspect aspect = selector.getAspect(SelectionAspect.class);
        (aspect.selectHandler != null ? aspect.selectHandler : aspect.defaultSelectHandler).execute(); // use dynamic handler if possible
    }

    public void handleCancel() {
        SelectionAspect aspect = selector.getAspect(SelectionAspect.class);
        aspect.selectHandler = null;
    }

    public void moveSelector(int dx, int dy, int dz) {
        cachePosition.set(selector.position);
        selector.position.add(dx, dy, dz);
        GameMvc.instance().model().get(LocalMap.class).normalizePosition(selector.position);
        if (!cachePosition.equals(selector.position)) selectorMoved();
    }

    private void selectorMoved() {
        validateAndUpdate();
        GameMvc.instance().view().localWorldStage.getCamera().handleSelectorMove();
    }

    /**
     * Places selector to the tile under given screen coordinates without changing z-level.
     */
    public void updateSelectorPositionByScreenCoords(int screenX, int screenY) {
        Vector3 modelCoords = castScreenPositionToModel(screenX, screenY);
        selector.position.set((int) modelCoords.x, (int) modelCoords.y, selector.position.z);
        GameMvc.instance().model().get(LocalMap.class).normalizePosition(selector.position);
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

    public void handleEKeyDown() {
        SelectorBoxAspect aspect = selector.getAspect(SelectorBoxAspect.class);
        if (aspect.enabled) {
            if(aspect.boxStart == null) {
                aspect.boxStart = selector.position.clone();
                GameMvc.instance().model().get(LocalMap.class).normalizePosition(aspect.boxStart);
            } else {
                handleSelection();
            }
        } else {
            aspect.boxStart = null;
            cancelSelectionBox();
        }
    }

    public void handleLeftTouchDown(int screenX, int screenY) {
        SelectorBoxAspect aspect = selector.getAspect(SelectorBoxAspect.class);
        if (aspect.enabled) {
            Vector3 modelCoords = castScreenPositionToModel(screenX, screenY);
            aspect.boxStart = new Position(modelCoords.x, modelCoords.y, selector.position.z);
            GameMvc.instance().model().get(LocalMap.class).normalizePosition(aspect.boxStart);
        } else {
            cancelSelectionBox();
        }
    }

    public void cancelSelectionBox() {
        selector.getAspect(SelectorBoxAspect.class).boxStart = null;
    }

    private Vector3 castScreenPositionToModel(int screenX, int screenY) {
        Vector3 batchCoords = GameMvc.instance().view().localWorldStage.getCamera().unproject(new Vector3(screenX, screenY, 0));
        AtlasesEnum atlas = AtlasesEnum.blocks; // use blocks sizes
        int heightToSkip = selector.position.z * atlas.HEIGHT + (atlas.hasToppings ? atlas.TOPPING_HEIGHT : 0);
        batchCoords.x /= atlas.WIDTH;
        batchCoords.y = (batchCoords.y - heightToSkip) / atlas.DEPTH;
        return batchCoords;
    }
}