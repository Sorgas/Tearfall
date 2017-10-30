package stonering.generators.plants;

import stonering.global.FileLoader;
import stonering.objects.plants.Tree;

/**
 * Created by Alexander on 19.10.2017.
 */
public class TreesGenerator {

    public Tree generateTree(String speciment, int age) {
        FileLoader.getTreesFile();
        return new Tree();
    }
}
