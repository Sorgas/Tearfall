package stonering.entity.local.crafting;

import java.util.ArrayList;

/**
 * One step of component selection for crafting an building.
 * Can be optional and have several variants.
 *
 * @author Alexander on 20.10.2018.
 */
public class CraftingComponentStep {
    private boolean optional;
    private ArrayList<CraftingComponentVariant> variants;

    public CraftingComponentStep() {}

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
