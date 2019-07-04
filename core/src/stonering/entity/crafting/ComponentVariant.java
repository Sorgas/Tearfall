package stonering.entity.crafting;

/**
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
