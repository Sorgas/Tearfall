package stonering.game.core.controller.controllers.designation;

import stonering.enums.ZoneTypesEnum;
import stonering.game.core.view.render.ui.menus.util.RectangleSelectComponent;

/**
 * @author Alexander on 04.03.2019.
 */
public class ZoneDesignationSequence extends DesignationSequence {
    private ZoneTypesEnum type;
    private RectangleSelectComponent rectangleSelectComponent;

    public ZoneDesignationSequence(ZoneTypesEnum type) {
        this.type = type;
//        rectangleSelectComponent = new RectangleSelectComponent()
    }

    @Override
    public void start() {

    }

    @Override
    public void end() {

    }

    @Override
    public void reset() {

    }

    @Override
    public String getText() {
        return null;
    }
}
