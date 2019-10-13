package stonering.entity.crafting;

import java.util.ArrayList;

/**
 * One step of component selection.
 * Can be optional and have several variants.
 *
 * @author Alexander on 20.10.2018.
 */
public class BuildingComponent {
    public String name;
    public ArrayList<ArrayList<String>> variants; // loaded from json
    public ArrayList<BuildingComponentVariant> componentVariants;

    public BuildingComponent() {
        variants = new ArrayList<>();
        componentVariants = new ArrayList<>();
    }

    public void init() {
        variants.forEach(
                strings -> componentVariants.add(new BuildingComponentVariant(
                        strings.get(0),
                        strings.get(1),
                        Integer.parseInt(strings.get(2)),
                        new int[]{Integer.parseInt(strings.get(3)), Integer.parseInt(strings.get(4))}))
        );
    }
}
