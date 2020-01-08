package stonering.entity.item;

/**
 * Item part. Has own condition.
 *
 * @author Alexander on 26.10.2018.
 */
public class ItemPart {
    public final String title; // displayed title
    public int material; // part material
    public float condition; // part condition // TODO replace with specific condition objects

    public ItemPart(String title, int material) {
        this.title = title;
        this.material = material;
    }
}
