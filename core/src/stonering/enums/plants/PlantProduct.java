package stonering.enums.plants;

import java.util.*;

/**
 * Represents item harvested from or dropped by plant when cut.
 * Plant product has tags, which are copied to item on creation.
 *
 * @author Alexander_Kuzyakov on 07.05.2019.
 */
public class PlantProduct {
    private static Random random = new Random();
    public String name;
    private Integer[] formulaArgs;
    public Set<Integer> months;
    public List<String> tags;
    public Map<String, String> aspectParams; // used to generate aspects of items/

    public PlantProduct() {
        tags = new ArrayList<>();
        months = new HashSet<>();
        formulaArgs = new Integer[3];
    }

    /**
     * Rolls the number of products by formula.
     */
    public int roll() {
        return (int) Math.max(Math.ceil((random.nextFloat() * formulaArgs[1] - formulaArgs[0]) / formulaArgs[2]), 0);
    }

    public void setFormulaArgs(Integer[] formulaArgs) {
        this.formulaArgs = formulaArgs;
    }
}
