package stonering.game.controller.controllers.designation;

import stonering.game.model.GameModel;
import stonering.game.model.entity_selector.aspect.SelectorBoxAspect;
import stonering.game.model.system.EntitySelectorSystem;
import stonering.screen.GameView;
import stonering.util.global.Logger;
import stonering.util.validation.PositionValidator;
import stonering.entity.zone.Zone;
import stonering.enums.ZoneTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.system.ZoneContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.stage.toolbar.menus.Toolbar;
import stonering.widget.RectangleSelectComponent;
import stonering.util.geometry.Position;

/**
 * Shows {@link RectangleSelectComponent} for selecting place for a new zone.
 * After this sequence, new {@link Zone} is created in {@link ZoneContainer}.
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
        view = GameMvc.instance().view();
        rectangleSelectComponent = new RectangleSelectComponent(null, event -> {
            EntitySelector selector = model.get(EntitySelectorSystem.class).selector;
            if (validateZoneDesignation()) {
                model.get(ZoneContainer.class).createNewZone(selector.getAspect(SelectorBoxAspect.class).boxStart, selector.position.clone(), type);
            } else {
                view.toolbarStage.toolbar.status.setText("No valid tiles selected");
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
            EntitySelector selector = model.get(EntitySelectorSystem.class).selector;
            ZoneContainer zoneContainer = GameMvc.instance().model().get(ZoneContainer.class);
            Zone zone = zoneContainer.getZone(selector.getAspect(SelectorBoxAspect.class).boxStart);
            Toolbar toolbar = view.toolbarStage.toolbar;
            toolbar.status.setText(zone != null ? "Expanding zone " + zone.getName() + "." : "Deleting zones.");
            return true;
        }, event -> {
            EntitySelector selector = model.get(EntitySelectorSystem.class).selector;
            ZoneContainer zoneContainer = GameMvc.instance().model().get(ZoneContainer.class);
            Zone zone = zoneContainer.getZone(selector.getAspect(SelectorBoxAspect.class).boxStart); // tiles in selected area will get this zone, and removed from previous.
            model.get(ZoneContainer.class).updateZones(selector.getAspect(SelectorBoxAspect.class).boxStart, selector.position.clone(), zone);
            return true;
        });
    }

    /**
     * Checks that zone has at least one valid tile.
     */
    private boolean validateZoneDesignation() {
        EntitySelector selector = model.get(EntitySelectorSystem.class).selector;
        LocalMap localMap = model.get(LocalMap.class);
        Position pos1 = selector.getAspect(SelectorBoxAspect.class).boxStart;
        Position pos2 = selector.position.clone();
        Position cachePos = new Position(0, 0, 0);
        PositionValidator validator = type.getValidator();
        for (cachePos.x = Math.min(pos1.x, pos2.x); cachePos.x <= Math.max(pos1.x, pos2.x); cachePos.x++) {
            for (cachePos.y = Math.min(pos1.y, pos2.y); cachePos.y <= Math.max(pos1.y, pos2.y); cachePos.y++) {
                for (cachePos.z = Math.min(pos1.z, pos2.z); cachePos.z <= Math.max(pos1.z, pos2.z); cachePos.z++) {
                    if (validator.validate(cachePos)) return true;
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
}
