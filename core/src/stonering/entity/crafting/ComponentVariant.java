package stonering.entity.crafting;

import stonering.entity.building.Blueprint;

/**
 * Variant for {@link BuildingComponent} of {@link Blueprint}.
 *
 * @author Alexander on 08.03.2019.
 */
public class ComponentVariant {
    private String tag;
    private int amount;
    private int[] atlasXY;

    public ComponentVariant(String tag, int amount, int[] atlasXY) {
        this.tag = tag;
        this.amount = amount;
        this.atlasXY = atlasXY;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int[] getAtlasXY() {
        return atlasXY;
    }

    public void setAtlasXY(int[] atlasXY) {
        this.atlasXY = atlasXY;
    }
}
