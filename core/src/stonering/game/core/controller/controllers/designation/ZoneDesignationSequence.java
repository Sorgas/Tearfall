package stonering.game.core.controller.controllers.designation;

import stonering.entity.local.building.validators.PositionValidator;
import stonering.entity.local.zone.Zone;
import stonering.enums.ZoneTypesEnum;
import stonering.game.core.GameMvc;
import stonering.game.core.model.EntitySelector;
import stonering.game.core.model.lists.ZonesContainer;
import stonering.game.core.model.local_map.LocalMap;
import stonering.game.core.view.render.ui.menus.toolbar.Toolbar;
import stonering.game.core.view.render.ui.menus.util.RectangleSelectComponent;
import stonering.util.geometry.Position;

/**
 * Shows {@link RectangleSelectComponent} for selecting place for a new zone.
 * After this sequence, new {@link Zone} is created in {@link ZonesContainer}.
 * No other widgets are shown, so zone should be configured with zone menu.
 *
 * @author Alexander on 04.03.2019.
 */
public class ZoneDesignationSequence extends DesignationSequence {
    private ZoneTypesEnum type;
    private RectangleSelectComponent rectangleSelectComponent;

    /**
     * Designates new zone with given type.
     */
    public ZoneDesignationSequence(ZoneTypesEnum type) {
        super();
        this.type = type;
        rectangleSelectComponent = new RectangleSelectComponent(null, event -> {
            EntitySelector selector = gameMvc.getModel().get(EntitySelector.class);
            if (validateZoneDesignation()) {
                gameMvc.getModel().get(ZonesContainer.class).createNewZone(selector.getFrameStart(), selector.getPosition(), type);
            } else {
                gameMvc.getView().getUiDrawer().getToolbar().setText("No valid tiles selected");
            }
            return true;
        });
    }

    /**
     * Will update existent zones. If rectangle is started on a zone, it is expanded.
     * Otherwise, selected tiles is removed from all zones.
     */
    public ZoneDesignationSequence() {
        super();
        rectangleSelectComponent = new RectangleSelectComponent(event -> { // updates toolbar text depending on start of selected frame
            EntitySelector selector = gameMvc.getModel().get(EntitySelector.class);
            ZonesContainer zonesContainer = GameMvc.getInstance().getModel().get(ZonesContainer.class);
            Zone zone = zonesContainer.getZone(selector.getFrameStart());
            Toolbar toolbar = gameMvc.getView().getUiDrawer().getToolbar();
            toolbar.setText(zone != null ? "Expanding zone " + zone.getName() + "." : "Deleting zones.");
            return true;
        }, event -> {
            EntitySelector selector = gameMvc.getModel().get(EntitySelector.class);
            ZonesContainer zonesContainer = GameMvc.getInstance().getModel().get(ZonesContainer.class);
            Zone zone = zonesContainer.getZone(selector.getFrameStart()); // tiles in selected area will get this zone, and removed from previous.
            gameMvc.getModel().get(ZonesContainer.class).updateZones(selector.getFrameStart(), selector.getPosition(), zone);
            return true;
        });
    }

    /**
     * Checks that zone has at least one valid tile.
     */
    private boolean validateZoneDesignation() {
        EntitySelector selector = gameMvc.getModel().get(EntitySelector.class);
        LocalMap localMap = gameMvc.getModel().get(LocalMap.class);
        Position pos1 = selector.getFrameStart();
        Position pos2 = selector.getPosition();
        Position cachePos = new Position(0, 0, 0);
        PositionValidator validator = type.getValidator();
        for (cachePos.x = Math.min(pos1.x, pos2.x); cachePos.x <= Math.max(pos1.x, pos2.x); cachePos.x++) {
            for (cachePos.y = Math.min(pos1.y, pos2.y); cachePos.y <= Math.max(pos1.y, pos2.y); cachePos.y++) {
                for (cachePos.z = Math.min(pos1.z, pos2.z); cachePos.z <= Math.max(pos1.z, pos2.z); cachePos.z++) {
                    if (validator.validate(localMap, cachePos)) return true;
                }
            }
        }
        return false;
    }

    @Override
    public void start() {
        System.out.println("starting zone");
        rectangleSelectComponent.show();
    }

    @Override
    public void end() {
        System.out.println("ending zone");
        rectangleSelectComponent.hide();
    }

    @Override
    public void reset() {
        System.out.println("resetting zone");
        end();
    }

    @Override
    public String getText() {
        return type != null ? "Select zone for " + type.name() + "." : "Modify zones.";
    }
}
