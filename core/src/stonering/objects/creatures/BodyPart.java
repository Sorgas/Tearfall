package stonering.objects.creatures;

/**
 * Created by Alexander on 19.10.2017.
 */
public class BodyPart {
    private String name;
    private int size;
    private int weight;

    public BodyPart(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
