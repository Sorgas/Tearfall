package stonering.game.model.entity_selector.tool;

import stonering.enums.OrientationEnum;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.util.geometry.Int3dBounds;
import stonering.util.geometry.RotationUtil;

/**
 * Tool, that provide some logic to {@link EntitySelector}.
 *
 * @author Alexander on 16.03.2020.
 */
public abstract class SelectionTool {
    private EntitySelector selector;
    protected OrientationEnum orientation;
    
    public abstract void handleSelection(Int3dBounds bounds); // called once for the whole selection box

    public void cancelSelection() {
        GameMvc.model().get(EntitySelectorSystem.class).selector.get(SelectionAspect.class).set(SelectionTools.SELECT);
    }

    public void rotate(boolean clockwise) {
        orientation = RotationUtil.rotate(orientation, clockwise);
    } // should be overridden for tools with rotation

    protected EntitySelector selector() {
        return selector != null ? selector : (selector = GameMvc.model().get(EntitySelectorSystem.class).selector);
    }
}
