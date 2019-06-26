package stonering.game.view.render.stages;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import stonering.entity.local.Entity;
import stonering.entity.local.building.Building;
import stonering.entity.local.building.BuildingBlock;
import stonering.entity.local.plants.AbstractPlant;
import stonering.entity.local.plants.Plant;
import stonering.entity.local.zone.Zone;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.lists.*;
import stonering.game.view.render.ui.lists.ObservingList;
import stonering.util.geometry.Position;
import stonering.util.global.Initable;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Stage for selecting entities on local map.
 * Has modes of selection for different entities groups.
 * Shows List of entities (units, item) if there is more than one.
 * Single entity con be selected from list.
 *
 * @author Alexander on 11.11.2018.
 */
public class MapEntitySelectStage extends UiStage implements Initable {
    public static final int NONE = -1;
    public static final int ITEMS = 0;
    public static final int UNITS = 1;
    public static final int PLANTS = 2;
    public static final int BUILDINGS = 3;
    public static final int ZONES = 4;

    private int activeMode;
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
    }

    /**
     * Fills list with entities on given position.
     */
    @Override
    public void init() {
        GameMvc gameMvc = GameMvc.instance();
        switch (activeMode) {
            case ITEMS:
                break;
            case UNITS:
                break;
            case PLANTS:
                break;
            case BUILDINGS:
                tryShowBuildingStage(gameMvc.getModel().get(BuildingContainer.class).getBuildingBlocks().get(currentPosition));
                break;
            case ZONES:
                break;
            case NONE:
                System.out.println("selecting zone");
                showEntitySelectList();
                return;
        }
        gameMvc.getView().removeStage(this);
    }

    /**
     * Observes map in current position.
     * If there is only one entity, shows it's stage.
     * IF there are several, shows select list.
     */
    private void collectEntities(List<Entity> entities) {
        GameModel gameModel = GameMvc.instance().getModel();
        BuildingBlock buildingBlock = gameModel.get(BuildingContainer.class).getBuildingBlocks().get(currentPosition);
        if (buildingBlock != null) entities.add(buildingBlock.getBuilding());
        AbstractPlant plant = gameModel.get(PlantContainer.class).getPlantInPosition(currentPosition);
        if(plant != null) entities.add(plant);
        entities.addAll(gameModel.get(ItemContainer.class).getItemsInPosition(currentPosition));
        entities.addAll(gameModel.get(UnitContainer.class).getUnitsInPosition(currentPosition));
        Zone zone = gameModel.get(ZonesContainer.class).getZone(currentPosition);
        if (zone != null) entities.add(zone);
    }

    private void showEntitySelectList() {
        List<Entity> entities = new ArrayList<>();
        collectEntities(entities);
        if (entities.size() == 1) {
            showEntityStage(entities.get(0));
        } else {
            createObservingList(entities);
        }
    }

    private void showEntityStage(Entity entity) {
        Logger.UI.logDebug("Showing stage for " + entity);
        if (entity instanceof Building) {
            tryShowBuildingStage(((Building) entity).getBlock());
        } else if (entity instanceof Zone) {
            tryShowZoneStage((Zone) entity);
        }
        //TODO add other entity types
    }

    private void tryShowBuildingStage(BuildingBlock block) {
        if (block == null) return;
        Building building = block.getBuilding();
        GameMvc gameMvc = GameMvc.instance();
        Logger.UI.logDebug("showing building stage for: " + building);
        gameMvc.getView().removeStage(this);
        gameMvc.getView().addStageToList(new BuildingStage(gameMvc, building));
        System.out.println("adding new building stage");
    }

    private void tryShowZoneStage(Zone zone) {
        if (zone == null) return;
        GameMvc gameMvc = GameMvc.instance();
        Logger.UI.logDebug("showing zone stage for: " + zone.getName());
        gameMvc.getView().removeStage(this);
        gameMvc.getView().addStageToList(new ZoneMenuStage(zone));
        System.out.println("adding new zone stage");
    }

    private void createObservingList(List<Entity> entities) {
        Logger.UI.logDebug("Creating entity selection list.");
        ObservingList observingList = new ObservingList(entities, aspectHolder -> showEntityStage(aspectHolder));
        Container container = new Container().center();
        container.setFillParent(true);
        container.setDebug(true, true);
        this.addActor(container);
        container.setActor(observingList);
        setKeyboardFocus(observingList);
    }
}