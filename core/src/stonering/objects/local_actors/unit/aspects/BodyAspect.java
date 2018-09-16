package stonering.objects.local_actors.unit.aspects;

import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.unit.Unit;

import java.util.ArrayList;

/**
 * @author Alexander Kuzyakov on 19.10.2017.
 *         <p>
 *         Holds creature's body parts and connections between them.
 */
public class BodyAspect extends Aspect {
    private ArrayList<BodyPart> bodyParts;
    private ArrayList<Integer> limbs;

    public BodyAspect(Unit unit) {
        super("body", unit);
        bodyParts = new ArrayList<BodyPart>();
    }

    public void addBodyPart(BodyPart bodyPart) {
        bodyParts.add(bodyPart);
    }

    public class BodyPart {
        public BodyPart(String name) {
            this.name = name;
        }

        public String name;
        public int size;
        public int weight;
        public String[] layers;
        public Organ[] organs;
    }

    public class Organ {
        public String name;
    }

    public ArrayList<BodyPart> getBodyParts() {
        return bodyParts;
    }

    public void setBodyParts(ArrayList<BodyPart> bodyParts) {
        this.bodyParts = bodyParts;
    }

    public ArrayList<Integer> getLimbs() {
        return limbs;
    }

    public void setLimbs(ArrayList<Integer> limbs) {
        this.limbs = limbs;
    }
}
