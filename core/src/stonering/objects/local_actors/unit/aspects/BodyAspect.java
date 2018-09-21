package stonering.objects.local_actors.unit.aspects;

import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.unit.Unit;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Holds creature's body parts and connections between them.
 *
 * @author Alexander Kuzyakov on 19.10.2017.
 */
public class BodyAspect extends Aspect {
    private String bodyType;
    private HashMap<String, BodyPart> bodyParts; // name to bodyPart
    private ArrayList<String> bodyPartsToCover;

    public BodyAspect(Unit unit) {
        super("body", unit);
        bodyParts = new HashMap<>();
        bodyPartsToCover = new ArrayList<>();
    }

    public class BodyPart {
        public String name;
        public int size;
        public int weight;
        public String type;
        public String[] layers;
        public Organ[] organs;
        public BodyPart root; // each body part points to one it`s connected to
        public String rootName;
        public String[] tags;
        public BodyPart(String name) {
            this.name = name;
        }

    }

    public class Organ {

        public String name;
    }


    public HashMap<String, BodyPart> getBodyParts() {
        return bodyParts;
    }

    public void setBodyParts(HashMap<String, BodyPart> bodyParts) {
        this.bodyParts = bodyParts;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public void setBodyPartsToCover(ArrayList<String> bodyPartsToCover) {
        this.bodyPartsToCover = bodyPartsToCover;
    }

    public ArrayList<String> getBodyPartsToCover() {
        return new ArrayList<>();
    }

    public void addBodyPart(BodyPart bodyPart) {
        bodyParts.put(bodyPart.name, bodyPart);
    }
}
