package stonering.stage.menu;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import stonering.TearFall;
import stonering.util.lang.StaticSkin;
import stonering.widget.ButtonMenu;
import stonering.widget.util.KeyNotifierListener;

/**
 * First menu of the game.
 *
 * @author Alexander on 7/7/2020
 */
public class MainMenu extends Table {
    private ButtonMenu menu;
    
    public MainMenu(TearFall game) {
        align(Align.left);
        // TODO add(new Label("Tearfall", StaticSkin.skin().get("caption", Label.LabelStyle.class)));
        add(new Label("Tearfall", StaticSkin.skin())).align(Align.left).height(80).fill().row();
        add().expandY().row();
        add(createMenu(game)).align(Align.bottomLeft);
        addListener(new KeyNotifierListener(menu));
    }

    private ButtonMenu createMenu(TearFall game) {
        menu = new ButtonMenu();
        menu.defaults().height(50).width(300).pad(10, 0, 0, 0);
        menu.pad(0, 10, 10, 10);
        menu.align(Align.bottomLeft);
        menu.addButton("Create world", Input.Keys.C, game::switchWorldGenMenu);
        if(emptyWorldExists()) menu.addButton("Start game", Input.Keys.E, game::switchWorldsSelectMenu);
        if(worldWithSettlementExists()) menu.addButton("Load game", Input.Keys.L, null);
        menu.addButton("About", Input.Keys.A, null);
        menu.addButton("Quit", Input.Keys.Q, Gdx.app::exit);
        return menu;
    }
    
    private boolean worldWithSettlementExists() {
        File file = new File("saves");
        return file.exists() && file.listFiles() != null;
    }
    
    private boolean emptyWorldExists() {
        File file = new File("saves");
        return file.exists() && file.listFiles() != null;
    }
}
