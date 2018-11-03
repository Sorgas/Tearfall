package stonering.entity.local.crafting;

/**
 * Variant of crafting component for one crafting step.
 *
 * @author Alexander on 20.10.2018.
 */
public class CraftingComponentVariant {
//    private String type;  item type is non-MVP
    private String material;
    private int amount;
    private int[] sprite;

    public CraftingComponentVariant() {}
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int[] getSprite() {
        return sprite;
    }

    public void setSprite(int[] sprite) {
        this.sprite = sprite;
    }
}
