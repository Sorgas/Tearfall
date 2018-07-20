package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import stonering.game.core.GameMvc;
import stonering.game.core.view.ui_components.lists.MaterialSelectList;
import stonering.game.core.view.ui_components.lists.ItemsCountList;
import stonering.utils.global.StaticSkin;

/**
 * Contains all general orders menus.
 * @author Alexander Kuzyakov on 17.06.2018.
 */
public class Toolbar extends HorizontalGroup implements Invokable {
    private GameMvc gameMvc;
    private ParentMenu parentMenu;
    private MaterialSelectList materialSelectList;
    private Label notification;

    public Toolbar(GameMvc gameMvc) {
        this.gameMvc = gameMvc;
        this.align(Align.bottom);
    }

    public void init() {
        parentMenu = new ParentMenu(gameMvc);
        parentMenu.init();
        parentMenu.show();
        materialSelectList = new MaterialSelectList(gameMvc);
        materialSelectList.init();
        notification = new Label("", StaticSkin.getSkin());
    }

    public ParentMenu getParentMenu() {
        return parentMenu;
    }

    /**
     * Levels counted from right to left, widget indexes is opposite.
     *
     * @param menu
     */
    public void addMenu(Actor menu) {
        System.out.println(menu.getClass().getName() + " shown");
        this.addActorAt(0, menu);
    }

    /**
     * Removes given menu and all actors to the left.
     *
     * @param menu
     */
    public void hideMenu(Actor menu) {
        int index = getChildren().indexOf(menu, true);
        while (getChildren().contains(menu, true)) {
            System.out.println(menu.getClass().getName() + " hidden");
            removeActor(getChildren().get(0));
        }
    }

    /**
     * Returns visible menu with lowest level (most left one).
     *
     * @return
     */
    public Invokable getActiveMenu() {
        for (int i = 0; i < getChildren().size; i++) {
            if (Invokable.class.isAssignableFrom(getChildren().get(i).getClass())) {
                return (Invokable) getChildren().get(i);
            }
        }
        return null;
    }

    public boolean isMaterialSelectShown() {
        return getChildren().contains(materialSelectList, true);
    }

    public ItemsCountList getMaterialSelectList() {
        return materialSelectList;
    }

    /**
     * Input entry point from {@link stonering.game.core.controller.controllers.ToolBarController}.
     * Simply transfers event to current active menu.
     * @param keycode pressed character.
     * @return true, if press handled
     */
    @Override
    public boolean invoke(int keycode) {
        return getActiveMenu().invoke(keycode);
    }
}
