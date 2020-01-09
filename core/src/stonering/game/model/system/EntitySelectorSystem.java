package stonering.game.model.system;

import com.badlogic.gdx.math.Vector3;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.entity_selector.aspect.ValidationAspect;
import stonering.game.model.local_map.LocalMap;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.geometry.Position;

/**
 * System that handles input passed to {@link EntitySelector}, does movement, validation and activation of selector.
 * Validation and activation is made in selector's aspects.
 *
 * @author Alexander on 10.01.2020
 */
public class EntitySelectorSystem implements ModelComponent {
    public final EntitySelector selector;
    private Position cachePosition;

    public EntitySelectorSystem() {
        selector = new EntitySelector();
        cachePosition = new Position();
    }

    /**
     * Validates and updates render aspect of selector.
     */
    public boolean validateAndUpdate() {
        ValidationAspect aspect = selector.getAspect(ValidationAspect.class);
        return aspect.validator.validateAnd(selector.position, aspect.validationConsumer);
    }


    public void handleSelection() {
        SelectionAspect aspect = selector.getAspect(SelectionAspect.class);
        (aspect.selectHandler != null ? aspect.selectHandler : aspect.defaultSelectHandler).execute(); // use dynamic handler if possible
    }

    /**
     * Moves selector by deltas
     */
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
        Vector3 batchCoords = GameMvc.instance().view().localWorldStage.getCamera().unproject(new Vector3(screenX, screenY, 0));
        AtlasesEnum atlas = AtlasesEnum.blocks; // use blocks sizes
        int heightToSkip = selector.position.z * atlas.HEIGHT + (atlas.hasToppings ? atlas.TOPPING_HEIGHT : 0);
        int x = (int) batchCoords.x / atlas.WIDTH;
        int y = ((int) batchCoords.y - heightToSkip) / atlas.DEPTH;
        selector.position.set(x,y, selector.position.z);
        GameMvc.instance().model().get(LocalMap.class).normalizePosition(selector.position);
    }
}
