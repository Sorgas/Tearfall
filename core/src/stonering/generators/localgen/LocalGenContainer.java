package stonering.generators.localgen;

import stonering.entity.world.World;
import stonering.game.GameMvc;
import stonering.game.model.MainGameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

import java.util.ArrayList;

/**
 * Stores intermediate results of local generation.
 *
 * @author Alexander Kuzyakov on 29.08.2017.
 */
public class LocalGenContainer {
    public LocalGenConfig config;
    public MainGameModel model;

    public int localElevation;
    public int[][] roundedHeightsMap;
    public float[][] heightsMap;
    public float[] monthlyTemperatures;

    public ArrayList<Position> waterSources;
    public ArrayList<Position> waterTiles;

    public LocalGenContainer(LocalGenConfig config, World world) {
        this.config = config;
        localElevation = (int) (world.worldMap.getElevation(config.getLocation().x, config.getLocation().y) * config.getWorldToLocalElevationModifier());
        model = new MainGameModel(new LocalMap(config.areaSize, config.areaSize, localElevation + config.getAirLayersAboveGround()));
        model.createComponents(world);
        GameMvc.createInstance(model).init();
        waterTiles = new ArrayList<>();
        waterSources = new ArrayList<>();
        monthlyTemperatures = new float[12];
    }
}