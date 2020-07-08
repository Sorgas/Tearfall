package stonering.stage.menu;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import stonering.TearFall;
import stonering.entity.world.World;
import stonering.generators.worldgen.WorldGenConfig;
import stonering.generators.worldgen.WorldGeneratorContainer;
import stonering.screen.ui_components.MiniMap;
import stonering.screen.util.WorldCellInfo;
import stonering.screen.util.WorldSaver;
import stonering.util.global.StaticSkin;
import stonering.widget.ButtonMenu;
import stonering.widget.util.KeyNotifierListener;

/**
 * Menu for generating worlds. Has left side with world settings and left with world map.
 * 
 * @author Alexander on 08.07.2020.
 */
public class WorldGenMenu extends Table {
    private WorldGeneratorContainer worldGeneratorContainer;
    private World world;
    private long seed; // gets updated from ui
    private int worldSize = 100; // changed from ui
    private TearFall game;
    private Random random = new Random();
    private MiniMap minimap;
    private WorldCellInfo worldCellInfo = new WorldCellInfo();
    private ButtonMenu bottomMenu;
    private TextButton randomizeSeedButton;
    private TextField seedField;

    private Label worldInfoLabel;

    public WorldGenMenu(TearFall game) {
        this.game = game;
        seed = random.nextLong();
        align(Align.bottomLeft);
        add(createLeftPanel()).growY();
        add(createMinimap()).grow();
        createListener();
        setDebug(true, true);
    }
    
    private void checkInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            minimap.moveFocus(1, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            minimap.moveFocus(-1, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            minimap.moveFocus(0, 1);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            minimap.moveFocus(0, -1);
        }
    }

    private void updateWorldInfoToLabel() {
        if (world != null) {
            int x = minimap.getFocus().x;
            int y = minimap.getFocus().y;
            worldInfoLabel.setText(worldCellInfo.getCellInfo(x, y, Math.round(world.getWorldMap().getElevation(x, y)),
                    world.getWorldMap().getSummerTemperature(x, y),
                    world.getWorldMap().getRainfall(x, y)));
        }
    }

    private void generateWorld() {
        WorldGenConfig config = new WorldGenConfig(seed, worldSize, worldSize);
        worldGeneratorContainer = new WorldGeneratorContainer();
        worldGeneratorContainer.init(config);
        worldGeneratorContainer.runContainer();
        world = worldGeneratorContainer.getWorld();
    }

    private Table createLeftPanel() {
        Table menuTable = new Table();
        menuTable.add(new Label("Generate new world", StaticSkin.skin())).size(300, 80).colspan(2).row(); // caption
        menuTable.add(new Label("Seed: ", StaticSkin.skin())).size(300, 50).colspan(2).row(); // caption 2
        menuTable.add(seedField = new TextField(Long.toString(seed), StaticSkin.skin())).size(240, 50).fillX();
        menuTable.add(randomizeSeedButton = new TextButton("R", StaticSkin.skin())).size(50, 50).padLeft(10).row();
        randomizeSeedButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                seed = random.nextLong();
                seedField.setText(Long.toString(seed));
            }
        });
        
        menuTable.add(new Label("World size: ", StaticSkin.skin())).size(300, 50).colspan(2).row(); // caption 2
        Slider worldSizeSlider = new Slider(100, 500, 100, false, StaticSkin.skin());
        worldSizeSlider.setValue(worldSize);
        Label worldSizeLabel = new Label(Integer.toString(worldSize), StaticSkin.skin());
        worldSizeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int size = Math.round(((Slider) actor).getValue());
                worldSize = size;
                worldSizeLabel.setText(Integer.toString(size));
            }
        });
        menuTable.add(worldSizeSlider).size(200, 50);
        menuTable.add(worldSizeLabel).size(90, 50).padLeft(10).row();
        worldInfoLabel = new Label("", StaticSkin.skin());
        menuTable.add(worldInfoLabel).colspan(2).row();
        menuTable.add().expandY().colspan(2).row();
        menuTable.add(createBottomMenu()).fill().colspan(2);
        return menuTable;
    }
    
    private ButtonMenu createBottomMenu() {
        bottomMenu = new ButtonMenu();
        bottomMenu.defaults().height(50).width(300).pad(10, 0, 0, 0);
        bottomMenu.pad(10);
        bottomMenu.createButton("G: Generate", Input.Keys.G, () -> {
            generateWorld();
            minimap.setWorld(world);
        });
        bottomMenu.createButton("C: Save", Input.Keys.C, () -> {
            new WorldSaver().saveWorld(world);
            game.switchMainMenu();
        });
        bottomMenu.createButton("Q: Back", Input.Keys.Q, () -> game.switchMainMenu());
        return bottomMenu;
    }
    
    private MiniMap createMinimap() {
        minimap = new MiniMap(new Texture("sprites/map_tiles.png"));
        minimap.setWorld(world);
        return minimap;
    }
    
    private void createListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch(keycode) {
                    case Input.Keys.R : {
                        randomizeSeedButton.toggle();
                        return true;
                    }
                }
                return false;
            }
        });
        addListener(new KeyNotifierListener(bottomMenu)); // menu control
        addListener(new KeyNotifierListener(minimap)); // minimap control
    }
}
