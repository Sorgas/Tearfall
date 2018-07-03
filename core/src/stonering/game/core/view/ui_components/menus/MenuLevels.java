package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import stonering.game.core.GameMvc;
import stonering.game.core.view.ui_components.lists.MaterialSelectList;
import stonering.game.core.view.ui_components.lists.StringIntegerList;
import stonering.utils.global.StaticSkin;

import java.util.HashMap;

/**
 * @author Alexander Kuzyakov on 17.06.2018.
 */
public class MenuLevels extends HorizontalGroup {
    private GameMvc gameMvc;
    private Toolbar toolbar;
    private MaterialSelectList materialSelectList;
    private Label notification;

    public MenuLevels(GameMvc gameMvc) {
        this.gameMvc = gameMvc;
        this.align(Align.bottom);
    }

    public void init() {
        toolbar = new Toolbar(gameMvc);
        toolbar.init();
        toolbar.show();
        materialSelectList = new MaterialSelectList(gameMvc);
        materialSelectList.init();
        notification = new Label("", StaticSkin.getSkin());
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
     * Removes given menu and all actors to the left.
     *
     * @param menu
     */
    public void hideMenu(ButtonMenu menu) {
        int index = getChildren().indexOf(menu, true);
        while (getChildren().contains(menu, true)) {
            removeActor(getChildren().get(0));
        }
    }

    /**
     * Returns visible menu with lowest level (most left one).
     *
     * @return
     */
    public ButtonMenu getActiveMenu() {
        for (int i = 0; i < getChildren().size; i++) {
            if (ButtonMenu.class.isAssignableFrom(getChildren().get(i).getClass())) {
                return (ButtonMenu) getChildren().get(i);
            }
        }
        return null;
    }

    public void showMaterialSelect(String buildingTitle) {
        materialSelectList.refill(buildingTitle);
        if (materialSelectList.getItems().size > 0) {
            this.addActorAt(0, materialSelectList);
            materialSelectList.setSelectedIndex(0);
//            materialSelectList.
        } else {
            notification.setText("No materials for " + buildingTitle + " are available.");
            this.addActorAt(0, notification);
        }
    }

    public boolean isMaterialSelectShown() {
        return getChildren().contains(materialSelectList, true);
    }

    public StringIntegerList getMaterialSelectList() {
        return materialSelectList;
    }
}
