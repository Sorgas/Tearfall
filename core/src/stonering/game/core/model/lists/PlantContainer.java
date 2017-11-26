package stonering.game.core.model.lists;

import stonering.objects.plants.Plant;
import stonering.objects.plants.Tree;

import java.util.ArrayList;

/**
 * Created by Alexander on 09.11.2017.
 */
public class PlantContainer {
    private ArrayList<Tree> trees;
    private ArrayList<Plant> plants;

    public PlantContainer(ArrayList<Tree> trees, ArrayList<Plant> plants) {
        this.trees = trees;
        this.plants = plants;
    }

    public ArrayList<Tree> getTrees() {
        return trees;
    }

    public ArrayList<Plant> getPlants() {
        return plants;
    }
}
