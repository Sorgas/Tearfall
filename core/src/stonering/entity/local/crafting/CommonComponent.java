package stonering.entity.local.crafting;

import stonering.util.global.Initable;

import java.util.ArrayList;

/**
 * One step of component selection.
 * Can be optional and have several variants.
 *
 * @author Alexander on 20.10.2018.
 */
public class CommonComponent implements Initable {
    private boolean optional;
    private String name;
    private ArrayList<ArrayList<String>> variants;
    private ArrayList<ComponentVariant> componentVariants;

    public CommonComponent() {
        variants = new ArrayList<>();
        componentVariants = new ArrayList<>();
    }

    @Override
    public void init() {
        variants.forEach(
                strings -> componentVariants.add(new ComponentVariant(strings.get(0),
                        Integer.parseInt(strings.get(1)),
                        new int[]{Integer.parseInt(strings.get(2)), Integer.parseInt(strings.get(3))}))
        );
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ArrayList<String>> getVariants() {
        return variants;
    }

    public void setVariants(ArrayList<ArrayList<String>> variants) {
        this.variants = variants;
    }

    public ArrayList<ComponentVariant> getComponentVariants() {
        return componentVariants;
    }

    public void setComponentVariants(ArrayList<ComponentVariant> componentVariants) {
        this.componentVariants = componentVariants;
    }
}
