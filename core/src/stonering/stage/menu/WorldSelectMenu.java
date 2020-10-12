package stonering.stage.menu;

import java.io.File;
import java.util.Optional;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import stonering.TearFall;
import stonering.entity.world.World;
import stonering.screen.ui_components.WorldMiniMap;
import stonering.screen.ui_components.WorldListItem;
import stonering.screen.util.WorldSaver;
import stonering.util.lang.StaticSkin;
import stonering.widget.ButtonMenu;
import stonering.widget.lists.NavigableList;
import stonering.widget.util.KeyNotifierListener;

/**
 * @author Alexander on 12.10.2020.
 */
public class WorldSelectMenu extends Table {
    private TearFall game;
    private World world;
    
    private NavigableList<WorldListItem> worldList;
    private ButtonMenu buttonMenu;
    private WorldMiniMap minimap;
    
    public WorldSelectMenu(TearFall game) {
        this.game = game;
        createLayout();
        addListener(new KeyNotifierListener(worldList));
        addListener(new KeyNotifierListener(buttonMenu));
    }

    private void createLayout() {
        defaults().fill().expandY();
        align(Align.bottomLeft);
        add(createLeftPanel());
        add(createRightPanel()).expandX();
    }

    private Table createLeftPanel() {
        Table leftPanel = new Table();
        leftPanel.align(Align.bottomLeft);
        leftPanel.add(new Label("Select world:", StaticSkin.getSkin())).growX().row();
        leftPanel.add(createWorldList()).growX().row();
        leftPanel.add().expandY().row();
        leftPanel.add(createButtonMenu());
        return leftPanel;
    }
    
    private Table createRightPanel() {
        Table rightPanel = new Table();
        rightPanel.align(Align.topLeft);
        rightPanel.add(new Label("World name", StaticSkin.getSkin())).growX().row(); // TODO
        rightPanel.add(new Label("World description", StaticSkin.getSkin())).growX().row(); // TODO
        rightPanel.add(createMinimap()).grow();
        return rightPanel;
    }

    private ButtonMenu createButtonMenu() {
        buttonMenu = new ButtonMenu();
        buttonMenu.defaults().height(50).width(300).pad(10, 0, 0, 0);
        buttonMenu.pad(0, 10, 10, 10);
        buttonMenu.addButton("Proceed", Input.Keys.E, () -> game.switchLocationSelectMenu(world));
        buttonMenu.addButton("Back", Input.Keys.Q, () -> game.switchMainMenu());
        return buttonMenu;
    }
    
    private NavigableList<WorldListItem> createWorldList() {
        worldList = new NavigableList<>();
        worldList.setItems(getWorldListItems());
        worldList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                world = new WorldSaver().loadWorld(((List<WorldListItem>) actor).getSelected().getTitle());
                minimap.setWorld(world);
            }
        });
        if (!worldList.getItems().isEmpty()) {
            worldList.setSelected(worldList.getItems().get(0));
        }
        return worldList;
    }

    private Container createMinimap() {
        minimap = new WorldMiniMap(new Texture("sprites/map_tiles.png"));
        Optional.ofNullable(worldList.getSelected())
                .ifPresent(item -> {
                    world = new WorldSaver().loadWorld(item.getTitle());
                    minimap.setWorld(world);
                });
        return minimap;
    }
    
    public Array<WorldListItem> getWorldListItems() {
        File root = new File("saves");
        Array<WorldListItem> list = new Array<>();
        if (root.exists()) {
            Optional.ofNullable(root.listFiles())
                    .ifPresent(saves -> {
                        for (File file : saves) {
                            Optional.ofNullable(file.listFiles())
                                    .ifPresent(save -> {
                                        if (save.length > 0) {
                                            list.add(new WorldListItem(file.getName(), file));
                                        }
                                    });
                        }
                    });
        }
        return list;
    }
}
