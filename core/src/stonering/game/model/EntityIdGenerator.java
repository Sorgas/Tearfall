package stonering.game.model;

import stonering.game.model.system.ModelComponent;

/**
 * @author Alexander on 21.07.2020.
 */
public class EntityIdGenerator implements ModelComponent {
    private static int id = 1;
    
    public static int get() {
        return id++;
    }
}
