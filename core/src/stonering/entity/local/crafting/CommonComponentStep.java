package stonering.entity.local.crafting;

import java.util.ArrayList;

/**
 * One step of component selection.
 * Can be optional and have several variants.
 *
 * @author Alexander on 20.10.2018.
 */
public class CommonComponentStep {
    private boolean optional;
    private String name;
    private ArrayList<ArrayList<String>> variants;

    public CommonComponentStep() {}

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
}
