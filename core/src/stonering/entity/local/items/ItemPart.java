package stonering.entity.local.items;

/**
 * Item part. Has own condition.
 *
 * @author Alexander on 26.10.2018.
 */
public class ItemPart {
    private String title;
    private int material;
    private float condition;
    private int volume;

    public ItemPart(String title, int material) {
        this.title = title;
        this.material = material;
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
}
