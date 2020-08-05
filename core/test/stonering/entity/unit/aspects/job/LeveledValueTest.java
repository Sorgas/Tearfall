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
    private static SkillValue leveledValue;

    @BeforeEach
    void setUp() {
        leveledValue = new SkillValue();
    }

    @Test
    void changeValueWithoutLevelUp() {
        Assert.assertEquals(leveledValue.level(), 0);
        leveledValue.changeValue(5);
        Assert.assertEquals(leveledValue.level(), 0);
    }

    @Test
    void changeValueWithLevelUp() {
        Assert.assertEquals(leveledValue.level(), 0);
        leveledValue.changeValue(10);
        Assert.assertEquals(leveledValue.level(), 1);
    }

    @Test
    void setLevelInRange() {
        Assert.assertEquals(leveledValue.level(), 0);
        leveledValue.setLevel(3);
        Assert.assertEquals(leveledValue.level(), 3);
        Assert.assertEquals(leveledValue.value(), 40);
    }

    @Test
    void setLevelLesserThanMin() {
        Assert.assertEquals(leveledValue.level(), 0);
        leveledValue.setLevel(-1);
        Assert.assertEquals(leveledValue.level(), 0);
        Assert.assertEquals(leveledValue.value(), 0);
    }

    @Test
    void setLevelMoreThanMax() {
        Assert.assertEquals(leveledValue.level(), 0);
        leveledValue.setLevel(6);
        Assert.assertEquals(leveledValue.level(), 4);
        Assert.assertEquals(leveledValue.value(), 70);
    }
}
