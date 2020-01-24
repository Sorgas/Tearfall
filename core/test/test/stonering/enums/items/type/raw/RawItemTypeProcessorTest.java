package test.stonering.enums.items.type.raw;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import stonering.enums.items.type.raw.RawItemType;
import stonering.enums.items.type.raw.RawItemTypeProcessor;

/**
 * @author Alexander on 24.01.2020.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RawItemTypeProcessorTest {
    private RawItemTypeProcessor processor;

    @BeforeAll
    void beforeAll() {
        processor = new RawItemTypeProcessor();
    }

    void testExtendItemType() {
        RawItemType rawParentType = new RawItemType();
        RawItemType rawType = new RawItemType();
    }
}
