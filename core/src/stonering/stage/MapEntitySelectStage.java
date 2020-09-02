package stonering.stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.sun.istack.NotNull;

import stonering.entity.Entity;
import stonering.entity.building.Building;
import stonering.entity.building.BuildingBlock;
import stonering.entity.building.aspects.WorkbenchAspect;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.unit.Unit;
import stonering.entity.zone.Zone;
import stonering.entity.zone.aspect.FarmAspect;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.system.ZoneContainer;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.system.plant.PlantContainer;
import stonering.game.model.system.unit.UnitContainer;
import stonering.stage.entity_menu.building.item_container.ItemContainerMenu;
import stonering.stage.entity_menu.building.overview.BuildingOverviewMenu;
import stonering.stage.entity_menu.building.workbench.WorkbenchMenu;
import stonering.stage.entity_menu.item.ItemMenu;
import stonering.stage.entity_menu.unit.UnitMenu;
import stonering.stage.entity_menu.zone.FarmZoneMenu;
import stonering.stage.util.SingleActorShadedStage;
import stonering.stage.util.SingleMenuCornerStage;
import stonering.stage.util.UiStage;
import stonering.util.geometry.Int3dBounds;
import stonering.util.logging.Logger;
import stonering.widget.TabbedPane;
import stonering.widget.lists.ObservingList;

/**
 * Stage for selecting entities on local map.
 * Shows List of entities (unit, item) if there is more than one.
 * Then stage for singe entity can be shown.
 *
 * @author Alexander on 11.11.2018.
 */
public class MapEntitySelectStage extends UiStage {

    public void showEntitySelectList(Int3dBounds box) {
        List<Entity> entities = collectEntities(box);
        if (entities.isEmpty()) { // hide stage immediately
            GameMvc.view().removeStage(this);
        } else if (entities.size() == 1) { // show entity stage
            showEntityStage(entities.get(0));
        } else { // show list with all entities
            createObservingList(entities);
        }
    }

    private List<Entity> collectEntities(Int3dBounds box) {
        List<Entity> entities = new ArrayList<>();
        GameModel model = GameMvc.model();
        box.iterate(position -> {
            entities.add(model.get(BuildingContainer.class).getBuilding(position));
            entities.add(model.get(PlantContainer.class).getPlantInPosition(position));
            entities.addAll(model.get(ItemContainer.class).getItemsInPosition(position));
            entities.addAll(model.get(UnitContainer.class).getUnitsInPosition(position));
            entities.add(model.get(ZoneContainer.class).getZone(position));
        });
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

    private void showEntityStage(Entity entity) {
        if (entity instanceof Building) {
            tryShowBuildingStage(((Building) entity).blocks[0][0]);
        } else if (entity instanceof Zone) {
            tryShowZoneStage((Zone) entity);
        } else if (entity instanceof Item) {
            tryShowItemStage((Item) entity);
        } else if (entity instanceof Unit) {
            tryShowUnitStage((Unit) entity);
        } else {
            Logger.UI.logWarn("entity " + entity + " is not supported in select stage"); //TODO add other entity types
        }
        GameMvc.view().removeStage(this);
    }

    private void tryShowBuildingStage(@NotNull BuildingBlock block) {
        TabbedPane pane = new TabbedPane();
        //TODO building description
        if (block.building.get(WorkbenchAspect.class) != null) {
            pane.add("Orders", new WorkbenchMenu(block.building));
        }
        if (block.building.get(ItemContainerAspect.class) != null) {
            pane.add("Items", new ItemContainerMenu(block.building.get(ItemContainerAspect.class)));
        }
        pane.add("Overview", new BuildingOverviewMenu(block.building));
        GameMvc.view().addStage(new SingleActorShadedStage<>(pane, false));
    }

    private void tryShowZoneStage(@NotNull Zone zone) {
        if (zone.has(FarmAspect.class)) {
            GameMvc.view().addStage(new SingleActorShadedStage<>(new FarmZoneMenu(zone), false));
        }
    }

    private void tryShowItemStage(@NotNull Item item) {
        GameMvc.view().addStage(new SingleActorShadedStage<>(new ItemMenu(item), false));
    }

    private void tryShowUnitStage(@NotNull Unit unit) {
        GameMvc.view().toolbarStage.rightBar.showEntityMenu(new UnitMenu(unit));
    }
}
