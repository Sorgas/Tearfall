package stonering.game.core.view.render.stages;

import com.badlogic.gdx.Input;
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
    public static final int ALL = -1;
    public static final int ITEMS = 0;
    public static final int UNITS = 1;
    public static final int PLANTS = 2;
    public static final int BUILDINGS = 3;

    private int activeMode;

    private GameMvc gameMvc;
    private ObservingList observingList;
    private Position currentPosition;

    /**
     * -1 mode means all entities.
     *
     * @param gameMvc
     * @param currentPosition
     * @param activeMode
     */
    public MapEntitySelectStage(GameMvc gameMvc, Position currentPosition, int activeMode) {
        super();
        this.gameMvc = gameMvc;
        this.currentPosition = currentPosition;
        this.activeMode = activeMode;
        observingList = new ObservingList(gameMvc);
    }

    @Override
    public boolean invoke(int keycode) {
        switch (keycode) {
            case Input.Keys.Q :
                gameMvc.getView().removeStage(this);
                return true;
        }
        return false;
    }

    /**
     * Fills list with entities on given position.
     */
    @Override
    public void init() {
        observingList.clear();
        switch (activeMode) {
            case ALL:
                //TODO add method for all categories
                tryShowBuildingStage(gameMvc.getModel().getLocalMap().getBuildingBlock(currentPosition));
                break;
            case ITEMS:
                break;
            case UNITS:
                break;
            case PLANTS:
                break;
            case BUILDINGS:
                tryShowBuildingStage(gameMvc.getModel().getLocalMap().getBuildingBlock(currentPosition));
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
