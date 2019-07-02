package stonering.entity.local.unit.aspects.body;

import stonering.enums.unit.BodyTemplate;

/**
 * Class of creature body part. stored in {@link BodyTemplate}. {@link BodyAspect} does not store body parts.
 *
 * @author Alexander_Kuzyakov on 02.07.2019.
 */
public class BodyPart {
    public String title;
    public BodyPart root; // each body part points to one it`s connected to
    public String type; // determines wear items, that can be equipped.
    public String[] layers; // tissue layers
    public String[] organs;
    public String[] tags;
    public Wound[] wounds;
    
    public int size;
    public int weight;

    public BodyPart(String title) {
        this.title = title;
    }

}
