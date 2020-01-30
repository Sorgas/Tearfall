package stonering.enums.images;

/**
 * Describes region of some texture.
 * For usage details see {@link DrawableMap#regionProducer}.
 *
 * @author Alexander_Kuzyakov on 13.05.2019.
 */
public class DrawableDescriptor {
    public String name;
    public String texture;
    public int[] bounds;

    public DrawableDescriptor() {}

    public DrawableDescriptor(String name, String texture, int[] bounds) {
        this.name = name;
        this.texture = texture;
        this.bounds = bounds;
    }
}
