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
    public ArrayList<ArrayList<String>> variants;
    public ArrayList<ComponentVariant> componentVariants;
    public boolean optional = false;

    public BuildingComponent() {
        variants = new ArrayList<>();
        componentVariants = new ArrayList<>();
    }

    public void init() {
        variants.forEach(
                strings -> componentVariants.add(new ComponentVariant(strings.get(0),
                        Integer.parseInt(strings.get(1)),
                        new int[]{Integer.parseInt(strings.get(2)), Integer.parseInt(strings.get(3))}))
        );
    }
}
