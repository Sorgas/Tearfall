package stonering.stage.menu;

import java.util.Random;

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
    
    public WorldGenMenu(TearFall game) {
        this.game = game;
        seed = random.nextLong();
        align(Align.bottomLeft);
        add(createLeftPanel()).growY();
        add(createMinimap()).grow();
        createListener();
        setDebug(true, true);
    }

    private void generateWorld() {
        WorldGenConfig config = new WorldGenConfig(seed, worldSize, worldSize);
        worldGeneratorContainer = new WorldGeneratorContainer(config);
        worldGeneratorContainer.runContainer();
        world = worldGeneratorContainer.getWorld();
    }

    private Table createLeftPanel() {
        Table table = new Table();
        table.defaults().size(300, 50);
        table.add(new Label("Generate new world", StaticSkin.skin())).row(); // caption
        table.add(new Label("Seed: ", StaticSkin.skin())).row(); // caption 2
        table.add(createSeedTable()).row();
        table.add(new Label("World size: ", StaticSkin.skin())).row(); // caption 2
        table.add(createWorldSizeTable()).row();
        
//        table.add(worldInfoLabel = new Label("", StaticSkin.skin())).row();
        
        table.add().expandY().row();
        ButtonMenu menu = createBottomMenu(); 
        table.add(menu).size(menu.getPrefWidth(), menu.getPrefHeight());
        return table;
    }

    private Table createSeedTable() {
        Table table = new Table();
        table.add(seedField = new TextField(Long.toString(seed), StaticSkin.skin())).size(240, 50).fillX();
        table.add(randomizeSeedButton = new TextButton("R", StaticSkin.skin())).size(50, 50).padLeft(10).row();
        randomizeSeedButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                seed = random.nextLong();
                seedField.setText(Long.toString(seed));
            }
        });
        return table;
    }

    private Table createWorldSizeTable() {
        Table table = new Table();
        Slider slider = new Slider(100, 500, 100, false, StaticSkin.skin());
        slider.setValue(worldSize);
        Label label = new Label(Integer.toString(worldSize), StaticSkin.skin());
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int size = Math.round(((Slider) actor).getValue());
                worldSize = size;
                label.setText(Integer.toString(size));
            }
        });
        table.add(slider).size(200, 50);
        table.add(label).size(90, 50).padLeft(10).row();
        return table;
    }

    private ButtonMenu createBottomMenu() {
        bottomMenu = new ButtonMenu();
        bottomMenu.defaults().height(50).width(300).pad(10, 0, 0, 0);
        bottomMenu.pad(10);
        bottomMenu.addButton("G: Generate", Input.Keys.G, () -> {
            generateWorld();
            minimap.setWorld(world);
        });
        bottomMenu.addButton("C: Save", Input.Keys.C, () -> {
            new WorldSaver().saveWorld(world);
            game.switchMainMenu();
        });
        bottomMenu.addButton("Q: Back", Input.Keys.Q, () -> game.switchMainMenu());
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
                switch (keycode) {
                    case Input.Keys.R: {
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
