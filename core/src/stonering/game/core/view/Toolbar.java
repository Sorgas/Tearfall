/*
 * Created by Alexander on .
 */

package stonering.game.core.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import stonering.utils.global.StaticSkin;

/**
 * Created by Alexander on 19.12.2017.
 *
 * component of toolbar and its state
 */
public class Toolbar extends Table {
    private DiggingMenu diggingMenu;

    public Toolbar() {
        super();
        createTable();
    }

    private void createTable() {
        this.defaults().padLeft(10);
        this.pad(10);
        this.setFillParent(true);
        this.right().bottom();

        diggingMenu = new DiggingMenu();
        diggingMenu.setVisible(false);

        this.add(diggingMenu);
        this.row();

        TextButton diggingButton = new TextButton("D: digging", StaticSkin.getSkin());
        diggingButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                diggingMenu.setVisible(!diggingMenu.isVisible());
            }
        });
        this.add(diggingButton).row();
    }

    public void openDiggingMenu() {
        diggingMenu.setVisible(true);
    }

    public boolean diggingMenuIsOpen() {
        return diggingMenu.isVisible();
    }
}
