package stonering.enums;

/**
 * @author Alexander Kuzyakov on 08.07.2018.
 */
public enum NumberRelationsEnum {
    EQUALS {
        public boolean check(int val1, int val2) {
            return val1 == val2;
        }
    },
    NOEQUALS {
        boolean check(int val1, int val2) {
            return val1 != val2;
        }
    },
    LESS {
        boolean check(int val1, int val2) {
            return val1 < val2;
        }
    },
    MORE {
        boolean check(int val1, int val2) {
            return val1 > val2;
        }
    };

    abstract boolean check(int val1, int val2);
}
