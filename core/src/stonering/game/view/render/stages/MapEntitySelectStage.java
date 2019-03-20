package stonering.game.view.render.stages;

import stonering.entity.local.building.BuildingBlock;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.view.render.stages.base.UiStage;
import stonering.game.view.render.ui.lists.ObservingList;
import stonering.util.geometry.Position;
import stonering.util.global.Initable;
import stonering.util.global.TagLoggersEnum;

/**
 * Stage for selecting entities on local map.
 * Has modes of selection for different entities groups.
 * Shows List of entities (units, items) if there is more than one.
 * Single entity con be selected from list.
 *
 * @author Alexander on 11.11.2018.
 */
public class MapEntitySelectStage extends UiStage implements Initable {
    public static final int ITEMS = 0;
    public static final int UNITS = 1;
    public static final int PLANTS = 2;
    public static final int BUILDINGS = 3;
    public static final int ZONES = 4;

    private int activeMode;

    private ObservingList observingList;
    private Position currentPosition;

    /**
     * -1 mode means all entities.
     *
     * @param currentPosition
     * @param activeMode
     */
    public MapEntitySelectStage(Position currentPosition, int activeMode) {
        super();
        this.currentPosition = currentPosition;
        this.activeMode = activeMode;
        observingList = new ObservingList();
    }

    /**
     * Fills list with entities on given position.
     */
    @Override
    public void init() {
        GameMvc gameMvc = GameMvc.getInstance();
        observingList.clear();
        switch (activeMode) {
            case ITEMS:
                return;
            case UNITS:
                return;
            case PLANTS:
                return;
            case BUILDINGS:
                tryShowBuildingStage(gameMvc.getModel().get(LocalMap.class).getBuildingBlock(currentPosition));
                return;
            case ZONES:
        }
        gameMvc.getView().removeStage(this);
    }

    private void tryShowBuildingStage(BuildingBlock buildingBlock) {
        if (buildingBlock == null) return;
        GameMvc gameMvc = GameMvc.getInstance();
        TagLoggersEnum.UI.logDebug("showing building stage for: " + buildingBlock.getBuilding());
        gameMvc.getView().removeStage(this);
        gameMvc.getView().addStageToList(new BuildingStage(gameMvc, buildingBlock.getBuilding()));
        System.out.println("adding new building stage");
    }
}
