package stonering.stage.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import stonering.TearFall;
import stonering.entity.world.World;
import stonering.screen.ui_components.WorldMiniMap;
import stonering.screen.util.WorldCellInfo;
import stonering.util.lang.StaticSkin;
import stonering.widget.ButtonMenu;
import stonering.widget.util.KeyNotifierListener;

/**
 * @author Alexander on 12.10.2020.
 */
public class LocationSelectMenu extends Table {
    private TearFall game;
    private WorldMiniMap minimap;
    private World world;
    private Label worldInfoLabel;
    private WorldCellInfo worldCellInfo;

    private ButtonMenu buttonMenu;

    public LocationSelectMenu(TearFall game, World world) {
        this.game = game;
        this.world = world;
        createLayout();
        worldCellInfo = new WorldCellInfo();
        addListener(createListener());
        addListener(new KeyNotifierListener(buttonMenu));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updateWorldInfoLabel();
    }

    private void createLayout() {
        setFillParent(true);
        align(Align.bottomLeft);
        add(createMenuTable()).growY();
        add(createMinimap()).expandX();
    }

    private Table createMenuTable() {
        Table menuTable = new Table();
        menuTable.defaults().fillX();
        menuTable.align(Align.bottomLeft);
        menuTable.add(new Label("Select location", StaticSkin.skin())).row();
        worldInfoLabel = new Label("", StaticSkin.skin());
        menuTable.add(worldInfoLabel).row();
        menuTable.add().expandY().row();
        menuTable.add(createButtonMenu()).fill();
        return menuTable;
    }

    private ButtonMenu createButtonMenu() {
        buttonMenu = new ButtonMenu();
        buttonMenu.defaults().height(50).width(300).pad(10, 0, 0, 0);
        buttonMenu.pad(0, 10, 10, 10);
        buttonMenu.addButton("Proceed", Input.Keys.E, () -> {
            game.switchPrepareExpeditionMenu(world, minimap.getFocus());
        });
        buttonMenu.addButton("Back", Input.Keys.Q, () -> {
            game.switchWorldsSelectMenu();
        });
        return buttonMenu;
    }

    private InputListener createListener() {
        return new InputListener() {

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                return handleKey(keycode);
            }

            @Override
            public boolean keyTyped(InputEvent event, char character) {
                return handleKey(charToKeycode(character));
            }
            
            private boolean handleKey(int keycode) {
                switch (keycode) {
                    case Input.Keys.W: {
                        minimap.moveFocus(0, 1);
                        return true;
                    }
                    case Input.Keys.A: {
                        minimap.moveFocus(-1, 0);
                        return true;
                    }
                    case Input.Keys.S: {
                        minimap.moveFocus(0, -1);
                        return true;
                    }
                    case Input.Keys.D: {
                        minimap.moveFocus(1, 0);
                        return true;
                    }
                }
                return false;
            }

            private int charToKeycode(char character) {
                return Input.Keys.valueOf(Character.valueOf(character).toString().toUpperCase());
            }
        };
    }
    
    private Container createMinimap() {
        minimap = new WorldMiniMap(new Texture("sprites/map_tiles.png"));
        minimap.setWorld(world);
        return minimap;
    }

    private void updateWorldInfoLabel() {
        if (world == null) return;
        int x = minimap.getFocus().x;
        int y = minimap.getFocus().y;
        worldInfoLabel.setText("qwer" + worldCellInfo.getCellInfo(x, y,
                Math.round(world.worldMap.getElevation(x, y)),
                world.worldMap.getSummerTemperature(x, y),
                world.worldMap.getRainfall(x, y)));
    }
}
