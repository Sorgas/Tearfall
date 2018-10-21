package stonering.game.core.view.render.ui.components.menus.util;

import com.badlogic.gdx.scenes.scene2d.ui.Button;

/**
 * Wrapper on button or submenu.
 *
 * @author Alexander on 21.10.2018.
 */
public class MenuItem extends Button {
    private String identifier;
    private Button button;
    private ButtonMenu menu;

    public MenuItem(String identifier, Button button) {
        this.identifier = identifier;
        this.button = button;
    }

    public MenuItem(String identifier, ButtonMenu menu) {
        this.identifier = identifier;
        this.menu = menu;
    }

    public void call() {
        if(button != null) {
            button.toggle();
        } else {
            menu.show();
        }
    }

    public void init(MenuItem menuItem) {
        if(menu != null) menu.init();
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void toggle() {

        super.toggle();

    }
}
