package stonering.entity.unit.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;

/**
 * Aspect for naming entities (mostly units). 
 * When entity info is displayed, this aspect should be preferably used.  
 * 
 * @author Alexander on 06.07.2020.
 */
public class NameAspect extends Aspect {
    public String name;
    
    public NameAspect(Entity entity) {
        super(entity);
    }
}
