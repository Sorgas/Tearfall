package stonering.game.model.entity_selector.tool;

import stonering.game.model.entity_selector.EntitySelector;
import stonering.util.geometry.Int3dBounds;

/**
 * Tool, that provide some logic to {@link EntitySelector}.
 * 
 * @author Alexander on 16.03.2020.
 */
public abstract class SelectionTool {
    public abstract void handleSelection(Int3dBounds bounds); // called once for the whole selection box
    
    public abstract void cancelSelection(); // called on Q/RMB
}
