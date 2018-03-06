package stonering.generators.localgen.generators;

import stonering.exceptions.MaterialNotFoundException;
import stonering.game.core.model.LocalMap;
import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.plants.TreesGenerator;
import stonering.objects.local_actors.plants.Tree;

import java.util.Random;

/**
 * Created by Alexander on 30.10.2017.
 *
 * Generates groups of plants
 */
public class LocalForestGenerator {
    private LocalGenContainer container;
    private LocalGenConfig config;
    private int localAreaSize;
    private LocalMap localMap;
    private Random random;
    private TreesGenerator treesGenerator;

    public LocalForestGenerator(LocalGenContainer container) {
        this.container = container;
        this.config = container.getConfig();
        this.localAreaSize = config.getAreaSize();
        this.localMap = container.getLocalMap();
        treesGenerator = new TreesGenerator(container);
    }

    public void execute() {
        random = new Random();
        for (int i = 0; i < 100; i++) {
            try {
                Tree tree = treesGenerator.generateTree("willow", 10);
                tree.setX(random.nextInt(localAreaSize - 10) + 5);
                tree.setY(random.nextInt(localAreaSize - 10) + 5);
                tree.setZ(container.getHeightsMap()[tree.getX()][tree.getY()]);
                container.getTrees().add(tree);
            } catch (MaterialNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private boolean inMap(int x, int y, int z) {
        return x < localAreaSize && y < localAreaSize && z < config.getAreaHight() && x >= 0 && y >= 0 && z >= 0;
    }
}