package stonering.enums.items.type;

/**
 * Specifies crafting info for one itemPart.
 *
 * @author Alexander on 27.10.2018.
 */
public class ItemPartType {
    public String title;

    private boolean required = true; // item parts are required by default

    //TODO allowed materials and volumes.

    public ItemPartType() {}

    public ItemPartType(String title, boolean required) {
        this.title = title;
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
