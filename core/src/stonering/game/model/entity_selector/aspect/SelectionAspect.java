package stonering.game.model.entity_selector.aspect;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.game.model.entity_selector.tool.SelectionToolEnum;
import stonering.game.model.entity_selector.tool.SelectionTool;

/**
 * Contains {@link SelectionTool} picked for selector.
 *
 * @author Alexander on 10.01.2020
 */
public class SelectionAspect extends Aspect {
    public SelectionTool tool;
    
    public SelectionAspect(Entity entity) {
        super(entity);
    }
    
    public void set(SelectionToolEnum tool) {
        this.tool = tool.TOOL;
    }
}
