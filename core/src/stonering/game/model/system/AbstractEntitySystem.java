package stonering.game.model.system;

import stonering.entity.Entity;

/**
 * Superclass for all systems that work with entities.
 * TODO add 'target' aspects
 *
 * @author Alexander on 01.11.2019.
 */
public abstract class AbstractEntitySystem<T extends Entity> {

    public abstract void update(T entity);
}
