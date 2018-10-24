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
    private ArrayList<CraftingComponentVariant> variants;

    public CommonComponentStep() {}

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public ArrayList<CraftingComponentVariant> getVariants() {
        return variants;
    }

    public void setVariants(ArrayList<CraftingComponentVariant> variants) {
        this.variants = variants;
    }
}
