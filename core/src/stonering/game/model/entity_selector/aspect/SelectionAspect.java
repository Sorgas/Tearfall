package stonering.game.model.entity_selector.aspect;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.entity_selector.SelectionToolEnum;
import stonering.game.model.entity_selector.tool.SelectionTool;

/**
 * Part of {@link EntitySelector} that contains selection box and logic for handling selection and cancellation.
 * <p>
 * Box last position is always {@link EntitySelector}'s position.
 * Handler is called once for selection box. Box iterator can be used to do some action with each tile in the box.
 * Box iterator also uses position validator.
 *
 * @author Alexander on 10.01.2020
 */
public class SelectionAspect extends Aspect {

    public SelectionAspect(Entity entity) {
        super(entity);
        enabled = true;
    }
    
    public void set(SelectionToolEnum tool) {
        this.tool = tool.TOOL;
    }
}
