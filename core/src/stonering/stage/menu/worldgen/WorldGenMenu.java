package stonering.stage.menu.worldgen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import stonering.TearFall;
import stonering.entity.world.World;
import stonering.generators.worldgen.WorldGenConfig;
import stonering.generators.worldgen.WorldGenSequence;
import stonering.screen.ui_components.WorldMiniMap;
import stonering.screen.util.WorldCellInfo;
import stonering.screen.util.WorldSaver;
import stonering.util.lang.StaticSkin;
import stonering.widget.ButtonMenu;
import stonering.widget.util.KeyNotifierListener;

/**
 * Menu for generating worlds. Has left side with world settings and left with world map.
 *
 * @author Alexander on 08.07.2020.
 */
public class WorldGenMenu extends Table {
    private final int INITIAL_WORLD_SIZE = 100;

    private final WorldGenSequence sequence;
    private final WorldGenConfig config;
    private WorldCellInfo worldCellInfo = new WorldCellInfo(); //todo

    private World world;
    private TearFall game;
    private WorldMiniMap minimap;
    private ButtonMenu bottomMenu;
    private Label worldNameLabel;
    
    public WorldGenMenu(TearFall game) {
        this.game = game;
        config = new WorldGenConfig(1, INITIAL_WORLD_SIZE, INITIAL_WORLD_SIZE);
        sequence = new WorldGenSequence(config);
        createLayout();
        addListener(new KeyNotifierListener(bottomMenu)); // menu control
        addListener(new KeyNotifierListener(minimap)); // minimap control
    }

    private void createLayout() {
        align(Align.bottomLeft);
        add(createLeftPanel()).growY();
        Table table = new Table();
        table.add(worldNameLabel = new Label("", StaticSkin.skin())).growX().row();
        table.add(createMinimap()).grow();
        add(table).grow();
        setDebug(true, true);
    }

    private Table createLeftPanel() {
        Table table = new Table();
        table.defaults().width(300);
        table.add(new WorldGenSettingsWidget(config)).row();
        table.add().growY().row();
        ButtonMenu menu = createBottomMenu();
        table.add(menu).size(menu.getPrefWidth(), menu.getPrefHeight());
        return table;
    }

    private ButtonMenu createBottomMenu() {
        bottomMenu = new ButtonMenu();
        bottomMenu.defaults().height(50).width(300).pad(10, 0, 0, 0);
        bottomMenu.pad(10);
        bottomMenu.addButton("G: Generate", Input.Keys.G, () -> {
            sequence.runGenerators();
            world = sequence.container.world;
            minimap.setWorld(world);
            worldNameLabel.setText(world.name);
        });
        bottomMenu.addButton("C: Save", Input.Keys.C, () -> {
            new WorldSaver().saveWorld(world);
            game.switchMainMenu();
        });
        bottomMenu.addButton("Q: Back", Input.Keys.Q, () -> game.switchMainMenu());
        return bottomMenu;
    }

    private WorldMiniMap createMinimap() {
        minimap = new WorldMiniMap(new Texture("sprites/map_tiles.png"));
        minimap.setWorld(world);
        return minimap;
    }
}
