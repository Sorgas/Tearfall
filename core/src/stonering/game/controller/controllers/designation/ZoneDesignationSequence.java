package stonering.game.controller.controllers.designation;

import stonering.game.model.GameModel;
import stonering.screen.GameView;
import stonering.util.global.Logger;
import stonering.util.validation.PositionValidator;
import stonering.entity.zone.Zone;
import stonering.enums.ZoneTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.EntitySelector;
import stonering.game.model.system.ZonesContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.stage.toolbar.menus.Toolbar;
import stonering.widget.RectangleSelectComponent;
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
    private GameModel model;
    private GameView view;

    /**
     * Designates new zone with given type.
     */
    public ZoneDesignationSequence(ZoneTypesEnum type) {
        super();
        this.type = type;
        model = GameMvc.instance().model();
        view = GameMvc.instance().getView();
        rectangleSelectComponent = new RectangleSelectComponent(null, event -> {
            EntitySelector selector = model.get(EntitySelector.class);
            if (validateZoneDesignation()) {
                model.get(ZonesContainer.class).createNewZone(selector.getFrameStart(), selector.getPosition(), type);
            } else {
                view.mainUiStage.toolbar.setText("No valid tiles selected");
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
            EntitySelector selector = model.get(EntitySelector.class);
            ZonesContainer zonesContainer = GameMvc.instance().model().get(ZonesContainer.class);
            Zone zone = zonesContainer.getZone(selector.getFrameStart());
            Toolbar toolbar = view.mainUiStage.toolbar;
            toolbar.setText(zone != null ? "Expanding zone " + zone.getName() + "." : "Deleting zones.");
            return true;
        }, event -> {
            EntitySelector selector = model.get(EntitySelector.class);
            ZonesContainer zonesContainer = GameMvc.instance().model().get(ZonesContainer.class);
            Zone zone = zonesContainer.getZone(selector.getFrameStart()); // tiles in selected area will get this zone, and removed from previous.
            model.get(ZonesContainer.class).updateZones(selector.getFrameStart(), selector.getPosition(), zone);
            return true;
        });
    }

    /**
     * Checks that zone has at least one valid tile.
     */
    private boolean validateZoneDesignation() {
        EntitySelector selector = model.get(EntitySelector.class);
        LocalMap localMap = model.get(LocalMap.class);
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
        Logger.ZONES.logDebug("starting zone");
        rectangleSelectComponent.show();
    }

    @Override
    public void end() {
        Logger.ZONES.logDebug("ending zone");
        rectangleSelectComponent.hide();
    }

    @Override
    public void reset() {
        Logger.ZONES.logDebug("resetting zone");
        end();
    }

    @Override
    public String getText() {
        return type != null ? "Select zone for " + type.name() + "." : "Modify zones.";
    }
}
