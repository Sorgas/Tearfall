package stonering.enums.plants;

import java.util.Random;
import java.util.Set;

/**
 * Represents item harvested from or dropped by plant when cut.
 *
 * @author Alexander_Kuzyakov on 07.05.2019.
 */
public class PlantProduct {
    private static Random random = new Random();
    public String name;
    private Integer[] formulaArgs;
    public Set<Integer> months;

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
