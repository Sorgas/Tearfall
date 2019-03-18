package stonering.game.core.controller.controllers.designation;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import stonering.entity.local.building.validators.PositionValidator;
import stonering.entity.local.zone.ZoneActor;
import stonering.enums.ZoneTypesEnum;
import stonering.game.core.model.EntitySelector;
import stonering.game.core.model.lists.ZonesContainer;
import stonering.game.core.model.local_map.LocalMap;
import stonering.game.core.view.render.ui.menus.util.RectangleSelectComponent;
import stonering.util.geometry.Position;

/**
 * Shows {@link RectangleSelectComponent} for selecting place for a new zone.
 * After this sequence, new {@link ZoneActor} is created in {@link ZonesContainer}.
 * No other widgets are shown, so zone should be configured with zone menu.
 *
 * @author Alexander on 04.03.2019.
 */
public class ZoneDesignationSequence extends DesignationSequence {
    private ZoneTypesEnum type;
    private RectangleSelectComponent rectangleSelectComponent;

    public ZoneDesignationSequence(ZoneTypesEnum type) {
        super();
        this.type = type;
        rectangleSelectComponent = new RectangleSelectComponent(event -> {
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
     * Checks that zone has at least one valid tile.
     */
    private boolean validateZoneDesignation() {
        EntitySelector selector = gameMvc.getModel().get(EntitySelector.class);
        LocalMap localMap = gameMvc.getModel().get(LocalMap.class);
        Position pos1 = selector.getFrameStart();
        Position pos2 = selector.getPosition();
        Position cachePos = new Position(0,0,0);
        PositionValidator validator = type.getValidator();
        for (cachePos.x = Math.min(pos1.x, pos2.x); cachePos.x <= Math.max(pos1.x, pos2.x); cachePos.x++) {
            for (cachePos.y = Math.min(pos1.y, pos2.y); cachePos.y <= Math.max(pos1.y, pos2.y); cachePos.y++) {
                for (cachePos.z = Math.min(pos1.z, pos2.z); cachePos.z <= Math.max(pos1.z, pos2.z); cachePos.z++) {
                    if(validator.validate(localMap, cachePos)) return true;
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
        return "Select zone for " + type.name() + ".";
    }
}
