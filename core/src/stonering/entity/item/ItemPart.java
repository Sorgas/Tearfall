package stonering.entity.item;

/**
 * Part of {@link Item}.
 * TODO add durability, state.
 *
 * @author Alexander on 5/28/2020
 */
public class ItemPart {
    private final Item item;
    public final String name;
    public int material;

    public ItemPart(Item item, String name, int material) {
        this.item = item;
        this.name = name;
        this.material = material;
    }
}
