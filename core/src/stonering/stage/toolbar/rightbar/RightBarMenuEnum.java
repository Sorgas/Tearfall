package stonering.stage.toolbar.rightbar;

import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * List of all menus for rightbar. Specifies constructor and button icon.
 * Menu for entity does not have it's button and not listed here.
 *
 * @author Alexander on 7/6/2020
 */
public enum RightBarMenuEnum {
    UNITS_MENU("units_button", GlobalJobsMenu::new),
    MILITARY_MENU("military_button", GlobalJobsMenu::new);

    public final String ICON_NAME;
    public final Supplier<Actor> SUPPLIER;

    RightBarMenuEnum(String iconName, Supplier<Actor> supplier) {
        ICON_NAME = iconName;
        SUPPLIER = supplier;
    }
}
