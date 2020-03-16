package stonering.game.model.system;

/**
 * System for updating game model. 
 * Unlike {@link EntitySystem}, does not perform any actions with entities. 
 *  
 * @author Alexander on 13.02.2020.
 */
public abstract class UtilitySystem extends System {

    public abstract void update();
}
