package stonering.objects.local_actors.unit;

/**
 * @author Alexander Kuzyakov on 19.10.2017.
 */
public class BodyPart {
    private String name;
    private int size;
    private int weight;
    private String[] layers;
    private Organ[] organs;

    public BodyPart(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String[] getLayers() {
        return layers;
    }

    public void setLayers(String[] layers) {
        this.layers = layers;
    }

    public Organ[] getOrgans() {
        return organs;
    }

    public void setOrgans(Organ[] organs) {
        this.organs = organs;
    }
}
