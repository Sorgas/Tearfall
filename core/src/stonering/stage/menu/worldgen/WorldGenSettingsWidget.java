package stonering.stage.menu.worldgen;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import stonering.generators.worldgen.WorldGenConfig;

/**
 * Widget with controls to change parameters of world generation.
 * 
 * @author Alexander on 19.10.2020.
 */
public class WorldGenSettingsWidget extends Table {
    private final WorldGenConfig config;

    public WorldGenSettingsWidget(WorldGenConfig config) {
        this.config = config;
    }
    
    
}
