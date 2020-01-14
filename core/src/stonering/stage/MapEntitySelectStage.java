package stonering.stage;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import stonering.entity.Entity;
import stonering.entity.building.Building;
import stonering.entity.building.BuildingBlock;
import stonering.entity.item.Item;
import stonering.entity.unit.Unit;
import stonering.entity.zone.FarmZone;
import stonering.entity.zone.Zone;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.system.*;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.plant.PlantContainer;
import stonering.game.model.system.unit.UnitContainer;
import stonering.stage.item.ItemStage;
import stonering.stage.unit.UnitStage;
import stonering.stage.workbench.BuildingStage;
import stonering.stage.zone.ZoneMenuStage;
import stonering.widget.lists.ObservingList;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Stage for selecting entities on local map.
 * Shows List of entities (unit, item) if there is more than one.
 * Then stage for singe entity can be shown.
 *
 * @author Alexander on 11.11.2018.
 */
public class MapEntitySelectStage extends UiStage {

    public void showEntitySelectList(Position position) {
        List<Entity> entities = collectEntities(position);
        // TODO close on empty list
        if (entities.size() == 1) {
            showEntityStage(entities.get(0));
        } else {
            createObservingList(entities);
        }
    }

    /**
     * Observes map in current position.
     * If there is only one entity, shows it's stage.
     * If there are several, shows select list.
     */
    private List<Entity> collectEntities(Position position) {
        List<Entity> entities = new ArrayList<>();
        GameModel model = GameMvc.instance().model();
        entities.add(model.get(BuildingContainer.class).getBuiding(position));
        entities.add(model.get(PlantContainer.class).getPlantInPosition(position));
        entities.addAll(model.get(ItemContainer.class).getItemsInPosition(position));
        entities.addAll(model.get(UnitContainer.class).getUnitsInPosition(position));
        entities.add(model.get(ZoneContainer.class).getZone(position));
        entities.removeIf(Objects::isNull);
        return entities;
    }

    private void createObservingList(List<Entity> entities) {
        Logger.UI.logDebug("Creating entity selection list.");
        ObservingList observingList = new ObservingList(entities, this::showEntityStage); // list will show stage for selected entity
        Container<ObservingList> container = new Container<>(observingList).center();
        container.setFillParent(true);
        container.setDebug(true, true);
        addActor(container);
        setKeyboardFocus(observingList);
    }

    /**
     * Shows stage according to entity type.
     */
    private void showEntityStage(Entity entity) {
        if (entity instanceof Building) {
            tryShowBuildingStage(((Building) entity).getBlock());
        } else if (entity instanceof Zone) {
            tryShowZoneStage((Zone) entity);
        } else if (entity instanceof Item) {
            tryShowItemStage((Item) entity);
        } else if (entity instanceof Unit) {
            tryShowUnitStage((Unit) entity);
        } else {
            //TODO add other entity types
            Logger.UI.logWarn("entity " + entity + " is not supported in select stage");
        }
        GameMvc.instance().view().removeStage(this);
    }

    private void tryShowBuildingStage(BuildingBlock block) {
        if (block == null) return;
        Building building = block.getBuilding();
        GameMvc.instance().view().addStage(new BuildingStage(building));
        Logger.UI.logDebug("showing building stage for: " + building);
    }

    private void tryShowZoneStage(Zone zone) {
        if (zone == null) return;
        GameMvc gameMvc = GameMvc.instance();
        Logger.UI.logDebug("showing zone stage for: " + zone.getName());
        if(zone instanceof FarmZone) gameMvc.view().addStage(new ZoneMenuStage((FarmZone) zone));
    }

    private void tryShowItemStage(Item item) {
        if (item == null) return;
        GameMvc gameMvc = GameMvc.instance();
        Logger.UI.logDebug("showing item stage for: " + item.title);
        gameMvc.view().addStage(new ItemStage(item));
    }

    private void tryShowUnitStage(Unit unit) {
        if (unit == null) return;
        GameMvc gameMvc = GameMvc.instance();
        Logger.UI.logDebug("showing unit stage for: " + unit);
        gameMvc.view().addStage(new UnitStage(unit));
    }
}
