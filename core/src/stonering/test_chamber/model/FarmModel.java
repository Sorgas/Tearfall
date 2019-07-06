package stonering.test_chamber.model;

import stonering.game.model.EntitySelector;

/**
 * Model for testing farms.
 *
 * @author Alexander_Kuzyakov on 04.07.2019.
 */
public class FarmModel extends TestModel {
    @Override
    public void init() {
        super.init();
        get(EntitySelector.class).setPosition(MAP_SIZE / 2, MAP_SIZE / 2, 2);
    }
}
