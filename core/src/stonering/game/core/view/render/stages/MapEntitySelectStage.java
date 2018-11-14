package stonering.game.core.view.render.stages;

import stonering.entity.local.building.BuildingBlock;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.lists.ObservingList;
import stonering.global.utils.Position;
import stonering.utils.global.TagLoggersEnum;

/**
 * Stage for selecting entities on local map.
 * Has modes of selection for different entities groups.
 * Shows List of entities (units, items) if there is more than one.
 * Single entity con be selected from list.
 *
 * @author Alexander on 11.11.2018.
 */
public class MapEntitySelectStage extends InvokableStage {
    public static final int ITEMS = 0;
    public static final int UNITS = 1;
    public static final int PLANTS = 2;
    public static final int BUILDINGS = 3;

    private GameMvc gameMvc;
    private ObservingList observingList;
    private int activeMode;

    public MapEntitySelectStage(GameMvc gameMvc) {
        this.gameMvc = gameMvc;
    }

    @Override
    public boolean invoke(int keycode) {
        return false;
    }

    /**
     * Fills list with entities on given position.
     *
     * @param position
     */
    public void init(Position position) {
        observingList.clear();
        switch (activeMode) {
            case ITEMS:
                break;
            case UNITS:
                break;
            case PLANTS:
                break;
            case BUILDINGS:
                tryShowBuildingStage(gameMvc.getModel().getLocalMap().getBuildingBlock(position));
                break;
        }
    }

    private void tryShowBuildingStage(BuildingBlock buildingBlock) {
        if (buildingBlock != null) {
            TagLoggersEnum.UI.logDebug("showing building stage for: " + buildingBlock.getBuilding().getName());
            gameMvc.getView().addStageToList(new BuildingStage(gameMvc, buildingBlock.getBuilding()));
        }
    }

    private void fillItems() {

    }

    private void fillUnits() {

    }

    private void fillBuildings(BuildingBlock buildingBlock) {

    }

    public void setActiveMode(int activeMode) {
        this.activeMode = activeMode;
    }
}
