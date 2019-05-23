package stonering.enums.items.recipe;

/**
 * Determines crafting of item part. Is part of {@link Recipe}.
 *
 * @author Alexander on 05.01.2019.
 */
public class ItemPartRecipe {
    public String name;
    public String materialTag;

    public ItemPartRecipe(String name, String materialTag) {
        this.name = name;
        this.materialTag = materialTag;
    }
}
