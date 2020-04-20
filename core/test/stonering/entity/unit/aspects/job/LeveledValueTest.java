package stonering.entity.unit.aspects.job;

import junit.framework.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * @author Alexander on 27.01.2020.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LeveledValueTest {
    private static int[] bounds = {10, 20, 40, 70};
    private static LeveledValue leveledValue;

    @BeforeEach
    void setUp() {
        leveledValue = new LeveledValue(bounds);
    }

    @Test
    void changeValueWithoutLevelUp() {
        Assert.assertEquals(leveledValue.getLevel(), 0);
        leveledValue.changeValue(5);
        Assert.assertEquals(leveledValue.getLevel(), 0);
    }

    @Test
    void changeValueWithLevelUp() {
        Assert.assertEquals(leveledValue.getLevel(), 0);
        leveledValue.changeValue(10);
        Assert.assertEquals(leveledValue.getLevel(), 1);
    }

    @Test
    void setLevelInRange() {
        Assert.assertEquals(leveledValue.getLevel(), 0);
        leveledValue.setLevel(3);
        Assert.assertEquals(leveledValue.getLevel(), 3);
        Assert.assertEquals(leveledValue.getValue(), 40);
    }

    @Test
    void setLevelLesserThanMin() {
        Assert.assertEquals(leveledValue.getLevel(), 0);
        leveledValue.setLevel(-1);
        Assert.assertEquals(leveledValue.getLevel(), 0);
        Assert.assertEquals(leveledValue.getValue(), 0);
    }

    @Test
    void setLevelMoreThanMax() {
        Assert.assertEquals(leveledValue.getLevel(), 0);
        leveledValue.setLevel(6);
        Assert.assertEquals(leveledValue.getLevel(), 4);
        Assert.assertEquals(leveledValue.getValue(), 70);
    }
}