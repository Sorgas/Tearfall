package stonering.game.core.view.render.stages;

import stonering.entity.local.building.Building;
import stonering.game.core.GameMvc;

/**
 * @author Alexander on 09.11.2018.
 */
public class BuildingStage extends InvokableStage {
    private GameMvc gameMvc;
    private Building building;

    public BuildingStage(GameMvc gameMvc, Building building) {
        this.gameMvc = gameMvc;
        this.building = building;
    }

    @Override
    public boolean invoke(int keycode) {
        return false;
    }

    public void init() {
    }
}
