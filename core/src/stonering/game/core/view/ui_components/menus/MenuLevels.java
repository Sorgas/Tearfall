package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.utils.Align;
import stonering.game.core.GameMvc;

/**
 * @author Alexander Kuzyakov on 17.06.2018.
 */
public class MenuLevels extends HorizontalGroup {
    private GameMvc gameMvc;
    private Toolbar toolbar;
    private MaterialSelectList materialSelectList;

    public MenuLevels(GameMvc gameMvc) {
        this.gameMvc = gameMvc;
        this.align(Align.bottom);
    }

    public void init() {
        toolbar = new Toolbar(gameMvc);
        toolbar.init();
        toolbar.show();
        materialSelectList = new MaterialSelectList();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    /**
     * Levels counted from right to left, widget indexes is opposite.
     *
     * @param menu
     */
    public void addMenu(ButtonMenu menu) {
        this.addActorAt(0, menu);
    }

    /**
     * Returns visible menu with lowest level
     *
     * @return
     */
    public ButtonMenu getActiveMenu() {
        return (ButtonMenu) getChildren().get(0);
    }

    public void showMaterialSelect() {
        this.addActorAt(0, materialSelectList);
    }
}
