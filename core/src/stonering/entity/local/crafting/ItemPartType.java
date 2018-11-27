package stonering.entity.local.crafting;

/**
 * Specifies crafting info for one itemPart.
 *
 * @author Alexander on 27.10.2018.
 */
public class ItemPartType extends CommonComponentStep {
    private String title;
    private int volume;             // sm^3

    //TODO allowed materials and volumes.

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
