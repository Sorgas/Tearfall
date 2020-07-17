package stonering.entity;

import java.io.Serializable;

/**
 * Component of an {@link Entity}.
 * TODO remove turns from all aspects(move to systems).
 *
 * @author Alexander Kuzyakov on 10.10.2017.
 */
public abstract class Aspect implements Serializable {
    public Entity entity;

    public Aspect(Entity entity) {
        this.entity = entity;
    }
    
    public Aspect() {}
}
