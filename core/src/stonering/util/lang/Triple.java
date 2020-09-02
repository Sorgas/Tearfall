package stonering.util.lang;

import java.util.Objects;

/**
 * Triple object. Does not clone it's fields.
 *
 * @author Alexander on 02.09.2019.
 */
public class Triple<T1, T2, T3> implements Cloneable {
    private T1 a;
    private T2 b;
    private T3 c;

    public Triple(T1 a, T2 b, T3 c) {
        set(a, b, c);
    }

    public void set(T1 a, T2 b, T3 c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
        return Objects.equals(a, triple.a) &&
                Objects.equals(b, triple.b) &&
                Objects.equals(c, triple.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Triple<T1, T2, T3>(a, b, c);
    }

    public T1 getA() {
        return a;
    }

    public void setA(T1 a) {
        this.a = a;
    }

    public T2 getB() {
        return b;
    }

    public void setB(T2 b) {
        this.b = b;
    }

    public T3 getC() {
        return c;
    }

    public void setC(T3 c) {
        this.c = c;
    }
}
