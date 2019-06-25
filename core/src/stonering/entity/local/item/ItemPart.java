package stonering.entity.local.item;

/**
 * Item part. Has own condition.
 *
 * @author Alexander on 26.10.2018.
 */
public class ItemPart {
    private String title;
    private int material;
    private float condition;
    private int volume; // sm^3

    public ItemPart(String title, int material, int volume) {
        this.title = title;
        this.material = material;
        this.volume = volume;
        initCondition();
    }

    private void initCondition() {
        //TODO
    }

    public String getTitle() {
        return title;
    }

    public int getMaterial() {
        return material;
    }

    public float getCondition() {
        return condition;
    }

    public void setCondition(float condition) {
        this.condition = condition;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setMaterial(int material) {
        this.material = material;
    }
}
